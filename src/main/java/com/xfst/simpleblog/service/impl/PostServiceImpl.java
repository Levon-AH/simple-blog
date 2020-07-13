package com.xfst.simpleblog.service.impl;

import com.xfst.simpleblog.exception.SimpleBlogException;
import com.xfst.simpleblog.repository.PostRepository;
import com.xfst.simpleblog.repository.UserRepository;
import com.xfst.simpleblog.repository.data.PostDAO;
import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.service.PostService;
import com.xfst.simpleblog.service.data.PostDTO;
import com.xfst.simpleblog.utils.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
            postDAO = new PostDAO();
            postDAO.setCreatedTime(new Date());
            setDaoFieldsFromDto(postDAO, post);
            postDAO = postRepository.save(postDAO);
        } catch (Exception ex) {
            throw new SimpleBlogException("unexpected error");
        }
        return PostMapper.map(postDAO);
    }

    @Override
    public PostDTO findBy(String title) {
        if (StringUtils.isEmpty(title)) {
            throw new SimpleBlogException();
        }
        Optional<PostDAO> post = postRepository.findByTitle(title);
        return post.map(PostMapper::map).orElseThrow(SimpleBlogException::new);
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
            setDaoFieldsFromDto(postDAO, post);
            postDAO.setUpdatedTime(new Date());
            postDAO = postRepository.save(postDAO);
        } catch (Exception ex) {
            throw new SimpleBlogException("unexpected error");
        }
        return PostMapper.map(postDAO);
    }

    @Override
    public void delete(Long id) {
        PostDAO post = getById(id);
        post.setDeletedTime(new Date());
        post.setUpdatedTime(new Date());
        try {
            postRepository.save(post);
        } catch (Exception ex) {
            throw new SimpleBlogException();
        }
    }

    private void setDaoFieldsFromDto(final PostDAO dao, final PostDTO dto) {
        dao.setId(dto.getId());
        dao.setTitle(dto.getTitle());
        dao.setText(dto.getText());
        Optional<UserDAO> user = userRepository.findById(dto.getUserId());
        if (!user.isPresent()) {
            throw new SimpleBlogException();
        }
        dao.setUser(user.get());
    }

    private PostDAO getById(final Long id) {
        if (id == null) {
            throw new SimpleBlogException();
        }
        Optional<PostDAO> postDAO = postRepository.findById(id);
        return postDAO.orElseThrow(SimpleBlogException::new);
    }
}
