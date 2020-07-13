package com.xfst.simpleblog.controller;

import com.xfst.simpleblog.config.WithMockCustomUser;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockCustomUser(username = "admin")
@AutoConfigureTestEntityManager
@Transactional
class AdminControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TestEntityManager entityManager;

    private UserDAO user;

    @BeforeEach
    void init() {
        user = entityManager.merge(getUserDao(UUID.randomUUID().toString(),
                UUID.randomUUID().toString()));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void clear() {
        entityManager.clear();
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/admin/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    void blockUser() throws Exception {
        mockMvc.perform(post("/admin/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserPost() throws Exception {
        Long postId = user.getPosts().stream()
                .findFirst()
                .map(PostDAO::getId)
                .get();

        mockMvc.perform(delete("/admin/posts/" + postId + "/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private UserDAO getUserDao(final String username, final String email) {
        final UserDAO user = new UserDAO();
        user.setActive(true);
        user.setFirstName("test firstName");
        user.setLastName("test lastName");
        user.setUsername(username);
        user.setEmail(email);
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