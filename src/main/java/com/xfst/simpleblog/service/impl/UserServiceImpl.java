package com.xfst.simpleblog.service.impl;

import com.xfst.simpleblog.constants.ErrorCodes;
import com.xfst.simpleblog.constants.RoleType;
import com.xfst.simpleblog.exception.SimpleBlogException;
import com.xfst.simpleblog.repository.RoleRepository;
import com.xfst.simpleblog.repository.UserRepository;
import com.xfst.simpleblog.repository.data.Role;
import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.service.UserService;
import com.xfst.simpleblog.service.data.UserDTO;
import com.xfst.simpleblog.utils.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO create(UserDTO user) {
        UserDAO userDAO;
        try {
            log.info("trying to save user: {}", user);
            userDAO = new UserDAO();
            setDaoFieldsFromDto(userDAO, user);
            userDAO = userRepository.save(userDAO);
            log.info("successfully saved user: {}", user);
        } catch (Exception ex) {
            log.error("something happened during user creation");
            throw new SimpleBlogException(ErrorCodes.ERROR_UNEXPECTED);
        }
        return UserMapper.map(userDAO);
    }

    @Override
    public UserDTO findBy(Long id) {
        return UserMapper.map(getById(id));
    }

    @Override
    public UserDTO findBy(String username) {
        if (StringUtils.isEmpty(username)) {
            log.warn("invalid username for user: {}", username);
            throw new SimpleBlogException(ErrorCodes.ERROR_USER_INVALID_USERNAME);
        }
        Optional<UserDAO> user = userRepository.findByUsername(username);
        return user
                .map(UserMapper::map)
                .<SimpleBlogException>orElseThrow(() -> {
                    log.warn("user not found for that username: {}", username);
                    throw new SimpleBlogException(ErrorCodes.ERROR_USER_NOT_FOUND);
                });
    }

    @Override
    public void blockBy(Long id) {
        UserDAO user = getById(id);
        user.setActive(false);
        try {
            userRepository.save(user);
            log.info("user blocked successfully");
        } catch (Exception ex) {
            log.error("something happened during user blocking");
            throw new SimpleBlogException(ErrorCodes.ERROR_UNEXPECTED);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private void setDaoFieldsFromDto(final UserDAO dao, final UserDTO dto) {
        dao.setId(dto.getId());
        dao.setActive(dto.isActive());
        dao.setFirstName(dto.getFirstName());
        dao.setLastName(dto.getLastName());
        dao.setEmail(dto.getEmail());
        dao.setUsername(dto.getUsername());
        dao.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role roleUser = roleRepository.findByName(RoleType.ROLE_USER.name());
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        dao.setRoles(roles);
        dao.setCreatedTime(new Date());
    }

    private UserDAO getById(Long id) {
        if (id == null || id <= 0) {
            log.warn("invalid id for user");
            throw new SimpleBlogException(ErrorCodes.ERROR_USER_INVALID_ID);
        }
        Optional<UserDAO> userDAO = userRepository.findById(id);
        return userDAO.<SimpleBlogException>orElseThrow(() -> {
            log.warn("user not found for that id: {}", id);
            throw new SimpleBlogException(ErrorCodes.ERROR_USER_NOT_FOUND);
        });
    }
}
