package com.xfst.simpleblog.service;

import com.xfst.simpleblog.service.data.UserDTO;

public interface UserService {
    UserDTO create(final UserDTO user);

    UserDTO findBy(final Long id);

    UserDTO findBy(final String username);

    void blockBy(final Long id);

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);
}
