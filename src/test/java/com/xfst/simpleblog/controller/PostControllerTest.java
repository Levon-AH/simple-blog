package com.xfst.simpleblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xfst.simpleblog.config.WithMockCustomUser;
import com.xfst.simpleblog.constants.ErrorCodes;
import com.xfst.simpleblog.controller.request.PostCreateRequest;
import com.xfst.simpleblog.controller.request.PostUpdateRequest;
import com.xfst.simpleblog.repository.data.PostDAO;
import com.xfst.simpleblog.repository.data.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockCustomUser(username = "admin")
@AutoConfigureTestEntityManager
@Transactional
class PostControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestEntityManager entityManager;

    private Long postId;

    @BeforeEach
    void init() {
        UserDAO user = entityManager.merge(getUserDao());
        postId = user.getPosts()
                .stream()
                .findFirst()
                .map(PostDAO::getId)
                .get();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void clear() {
        entityManager.clear();
    }

    @Test
    void create() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("The title");
        request.setText("The text");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }

    @Test
    void createWithInvalidTitle() throws Exception {
        PostCreateRequest request = new PostCreateRequest();
        request.setText("text");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("title must be not empty"));
    }

    @Test
    void edit() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("Updated title");
        request.setText("Updated text");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/post/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.post.title")
                        .value(request.getTitle()));
    }

    @Test
    void editWithInvalidText() throws Exception {
        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("title");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/post/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.text")
                        .value("text must be not empty"));
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(delete("/post/" + postId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeWithInvalidId() throws Exception {
        mockMvc.perform(delete("/post/" + -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCodes.ERROR_POST_INVALID_ID.getMessage()));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/post/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.posts[0].title")
                        .value(getPost().getTitle()));
    }

    private UserDAO getUserDao() {
        final UserDAO user = new UserDAO();
        user.setActive(true);
        user.setFirstName("test firstName");
        user.setLastName("test lastName");
        user.setUsername("test username");
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setCreatedTime(new Date());
        PostDAO post = getPost();
        post.setUser(user);
        user.setPosts(Collections.singletonList(post));
        return user;
    }

    private PostDAO getPost() {
        final PostDAO post = new PostDAO();
        post.setText("Test text");
        post.setTitle("Test title");
        post.setCreatedTime(new Date());
        return post;
    }
}