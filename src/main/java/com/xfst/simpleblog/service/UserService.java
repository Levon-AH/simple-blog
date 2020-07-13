package com.xfst.simpleblog.service;

import com.xfst.simpleblog.service.data.UserDTO;

public interface UserService {
    UserDTO create(final UserDTO user);

    UserDTO findBy(final String username);

    void deleteById(final Long id);
}
