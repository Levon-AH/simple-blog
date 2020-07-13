package com.xfst.simpleblog.controller;

import com.xfst.simpleblog.controller.request.PostBaseRequest;
import com.xfst.simpleblog.controller.request.PostCreateRequest;
import com.xfst.simpleblog.controller.request.PostUpdateRequest;
import com.xfst.simpleblog.controller.response.PostCreateResponse;
import com.xfst.simpleblog.controller.response.PostListResponse;
import com.xfst.simpleblog.controller.response.PostUpdateResponse;
import com.xfst.simpleblog.service.PostService;
import com.xfst.simpleblog.service.UserService;
import com.xfst.simpleblog.service.data.PostDTO;
import com.xfst.simpleblog.service.data.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "post",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostController {
    private final UserService userService;
    private final PostService postService;


    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostCreateResponse> post(@RequestBody @Valid final PostCreateRequest request,
                                                   Principal principal) {

        UserDTO user = userService.findBy(principal.getName());
        PostDTO postDto = createPostDto(request, user);
        PostDTO created = postService.create(postDto);
        PostCreateResponse response = new PostCreateResponse();
        response.setPost(created);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostUpdateResponse> edit(@RequestBody @Valid final PostUpdateRequest request,
                                                   @PathVariable("id") final Long id,
                                                   Principal principal) {
        UserDTO user = userService.findBy(principal.getName());
        PostDTO postDto = createPostDto(request, user);
        postDto.setId(id);
        PostDTO updated = postService.update(postDto);
        PostUpdateResponse response = new PostUpdateResponse();
        response.setPost(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        postService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAll() {
        List<PostDTO> posts = postService.findAll();
        final PostListResponse response = new PostListResponse();
        response.setPosts(posts);
        return ResponseEntity.ok(response);
    }

    private <T extends PostBaseRequest> PostDTO createPostDto(final T request, final UserDTO user) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(request.getTitle());
        postDTO.setText(request.getText());
        postDTO.setUserId(user.getId());
        return postDTO;
    }
}
