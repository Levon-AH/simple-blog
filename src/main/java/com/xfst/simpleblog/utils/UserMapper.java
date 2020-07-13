package com.xfst.simpleblog.utils;

import com.xfst.simpleblog.repository.data.UserDAO;
import com.xfst.simpleblog.service.data.UserDTO;

public class UserMapper {

    public static UserDTO map(final UserDAO userDAO) {
        if (userDAO == null) {
            return null;
        }
       final UserDTO userDTO = new UserDTO();
       userDTO.setId(userDAO.getId());
       userDTO.setActive(userDAO.isActive());
       userDTO.setPosts(PostMapper.map(userDAO.getPosts()));
       userDTO.setUsername(userDAO.getUsername());
       userDTO.setCreatedTime(userDAO.getCreatedTime());
       userDTO.setDeletedTime(userDAO.getDeletedTime());
       return userDTO;
    }

    private UserMapper() {
        throw new UnsupportedOperationException();
    }
}
