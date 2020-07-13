package com.xfst.simpleblog.controller.response;

import com.xfst.simpleblog.service.data.PostDTO;
import lombok.Data;

import java.util.List;

@Data
public class PostListResponse {
    List<PostDTO> posts;
}
