package com.xfst.simpleblog.controller;

import com.xfst.simpleblog.constants.RoleType;
import com.xfst.simpleblog.controller.request.AuthenticationRequest;
import com.xfst.simpleblog.controller.request.UserRegistrationRequest;
import com.xfst.simpleblog.controller.response.AuthenticationResponse;
import com.xfst.simpleblog.repository.UserRepository;
import com.xfst.simpleblog.repository.data.Role;
import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.security.jwt.JwtTokenProvider;
import com.xfst.simpleblog.service.UserService;
import com.xfst.simpleblog.service.data.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDTO userDetails = userService
                .findBy(authenticationRequest.getUsername());

        if (userDetails == null) {
            throw new UsernameNotFoundException("User with username: " + authenticationRequest.getUsername() + " not found");
        }

        final String token = jwtTokenProvider.createToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(userDetails.getUsername(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserDTO created = userService.create(from(request));
        return ResponseEntity.ok(created);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }
    }

    private UserDTO from(final UserRegistrationRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setActive(true);
        userDTO.setUsername(request.getUsername());
        userDTO.setFirstName(request.getFirstName());
        userDTO.setLastName(request.getLastName());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(request.getPassword());
        return userDTO;
    }
}

