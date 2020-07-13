package com.xfst.simpleblog.utils;

import com.xfst.simpleblog.repository.data.PostDAO;
import com.xfst.simpleblog.service.data.PostDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostMapper {

    public static PostDTO map(final PostDAO postDAO) {
        if (postDAO == null) {
            return null;
        }
        final PostDTO postDTO = new PostDTO();
        postDTO.setId(postDAO.getId());
        postDTO.setTitle(postDAO.getTitle());
        postDTO.setText(postDAO.getText());
        postDTO.setUserId(postDAO.getUser().getId());
        postDTO.setCreatedTime(postDAO.getCreatedTime());
        postDTO.setUpdatedTime(postDAO.getUpdatedTime());
        postDTO.setDeletedTime(postDAO.getDeletedTime());
        return postDTO;
    }

    public static List<PostDTO> map(final List<PostDAO> daos) {
        return Optional.ofNullable(daos)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(PostMapper::map)
                .collect(Collectors.toList());
    }

    private PostMapper() {
        throw new UnsupportedOperationException();
    }
}
