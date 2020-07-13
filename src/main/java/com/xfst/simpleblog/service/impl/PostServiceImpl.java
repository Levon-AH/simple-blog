package com.xfst.simpleblog.service.impl;

import com.xfst.simpleblog.constants.ErrorCodes;
import com.xfst.simpleblog.exception.SimpleBlogException;
import com.xfst.simpleblog.repository.PostRepository;
import com.xfst.simpleblog.repository.UserRepository;
import com.xfst.simpleblog.repository.data.PostDAO;
import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.service.PostService;
import com.xfst.simpleblog.service.data.PostDTO;
import com.xfst.simpleblog.utils.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostDTO create(PostDTO post) {
        PostDAO postDAO;
        try {
            log.info("trying to save post: {}", post);
            postDAO = new PostDAO();
            postDAO.setCreatedTime(new Date());
            setDaoFieldsFromDto(postDAO, post);
            postDAO = postRepository.save(postDAO);
            log.info("successfully saved post: {}", post);
        } catch (Exception ex) {
            log.error("something happened during post creation");
            throw new SimpleBlogException(ErrorCodes.ERROR_UNEXPECTED);
        }
        return PostMapper.map(postDAO);
    }

    @Override
    public PostDTO findBy(String title) {
        if (StringUtils.isEmpty(title)) {
            log.warn("invalid title for post: {}", title);
            throw new SimpleBlogException(ErrorCodes.ERROR_POST_INVALID_TITLE);
        }
        Optional<PostDAO> post = postRepository.findByTitle(title);
        return post
                .map(PostMapper::map)
                .<SimpleBlogException>orElseThrow(() -> {
                    log.warn("post not found for that title: {}", title);
                    throw new SimpleBlogException(ErrorCodes.ERROR_POST_NOT_FOUND);
                });
    }

    @Override
    public List<PostDTO> findAll() {
        return postRepository.findAllByDeletedTimeIsNullOrDeletedTimeAfter(new Date())
                .map(PostMapper::map)
                .orElse(Collections.emptyList());
    }

    @Override
    public PostDTO update(PostDTO post) {
        PostDAO postDAO = getById(post.getId());
        try {
            log.info("trying to update post: {}", post);
            setDaoFieldsFromDto(postDAO, post);
            postDAO.setUpdatedTime(new Date());
            postDAO = postRepository.save(postDAO);
            log.info("successfully updated post: {}", post);
        } catch (Exception ex) {
            log.error("something happened during post update");
            throw new SimpleBlogException(ErrorCodes.ERROR_UNEXPECTED);
        }
        return PostMapper.map(postDAO);
    }

    @Override
    public void delete(Long id) {
        PostDAO post = getById(id);
        post.setDeletedTime(new Date());
        post.setUpdatedTime(new Date());
        try {
            log.info("trying to save post: {}", post);
            postRepository.save(post);
        } catch (Exception ex) {
            log.error("something happened during post deleting");
            throw new SimpleBlogException(ErrorCodes.ERROR_UNEXPECTED);
        }
    }

    private void setDaoFieldsFromDto(final PostDAO dao, final PostDTO dto) {
        dao.setId(dto.getId());
        dao.setTitle(dto.getTitle());
        dao.setText(dto.getText());
        Optional<UserDAO> user = userRepository.findById(dto.getUserId());
        if (!user.isPresent()) {
            log.warn("user not found for that id: {}", dto.getUserId());
            throw new SimpleBlogException(ErrorCodes.ERROR_USER_INVALID_ID);
        }
        dao.setUser(user.get());
    }

    private PostDAO getById(final Long id) {
        if (id == null || id <= 0) {
            log.warn("invalid id for post");
            throw new SimpleBlogException(ErrorCodes.ERROR_POST_INVALID_ID);
        }
        Optional<PostDAO> postDAO = postRepository.findById(id);
        return postDAO.<SimpleBlogException>orElseThrow(() -> {
            log.warn("post not found for that iid: {}", id);
            throw new SimpleBlogException(ErrorCodes.ERROR_POST_NOT_FOUND);
        });
    }
}
