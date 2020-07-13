package com.xfst.simpleblog.service;

import com.xfst.simpleblog.service.data.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO create(final PostDTO post);

    PostDTO findBy(final String title);

    List<PostDTO> findAll();

    PostDTO update(final PostDTO post);

    void delete(final Long id);
}
