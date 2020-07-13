package com.xfst.simpleblog.service.impl;

import com.xfst.simpleblog.exception.SimpleBlogException;
import com.xfst.simpleblog.repository.UserRepository;
import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.service.PostService;
import com.xfst.simpleblog.service.UserService;
import com.xfst.simpleblog.service.data.PostDTO;
import com.xfst.simpleblog.service.data.UserDTO;
import com.xfst.simpleblog.utils.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;

    public UserServiceImpl(UserRepository userRepository, PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @Override
    public UserDTO create(UserDTO user) {
        UserDAO userDAO;
        try {
            userDAO = new UserDAO();
            setDaoFieldsFromDto(userDAO, user);
            userDAO = userRepository.save(userDAO);
        } catch (Exception ex) {
            throw new SimpleBlogException("unexpected error");
        }
        if (!user.getPosts().isEmpty()) {
            for (PostDTO post : user.getPosts()) {
                postService.create(post);
            }
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
            throw new SimpleBlogException("invalid username");
        }
        Optional<UserDAO> user = userRepository.findByUsername(username);
        return user
                .map(UserMapper::map)
                .orElseThrow(SimpleBlogException::new);
    }

    @Override
    public void blockBy(Long id) {
        UserDAO user = getById(id);
        user.setActive(false);
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new SimpleBlogException();
        }
    }

    private void setDaoFieldsFromDto(final UserDAO dao, final UserDTO dto) {
        dao.setId(dto.getId());
        dao.setActive(dto.isActive());
        dao.setUsername(dto.getUsername());
    }

    private UserDAO getById(Long id) {
        if (id == null) {
            throw new SimpleBlogException();
        }
        Optional<UserDAO> userDAO = userRepository.findById(id);
        return userDAO.orElseThrow(SimpleBlogException::new);
    }
}
