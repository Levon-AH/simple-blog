package com.xfst.simpleblog.controller;

import com.xfst.simpleblog.service.PostService;
import com.xfst.simpleblog.service.UserService;
import com.xfst.simpleblog.service.data.PostDTO;
import com.xfst.simpleblog.service.data.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "admin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AdminController {
    private final UserService userService;
    private final PostService postService;


    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") final Long id) {
        UserDTO user = userService.findBy(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> blockUser(@PathVariable("id") final Long id) {
        userService.blockBy(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}/user/{userId}")
    public ResponseEntity<?> deleteUserPost(@PathVariable("postId") final Long postId,
                                            @PathVariable("userId") final long userId) {
        UserDTO user = userService.findBy(userId);
        List<PostDTO> userPosts = user.getPosts()
                .stream()
                .filter(postDTO -> Objects.equals(postDTO.getId(), postId))
                .collect(Collectors.toList());
        if (userPosts.size() != 1) {
            return ResponseEntity.badRequest().body("invalid id for post: " + postId);
        }
        postService.delete(postId);

        return ResponseEntity.ok().build();
    }
}
