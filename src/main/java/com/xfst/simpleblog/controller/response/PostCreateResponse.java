package com.xfst.simpleblog.controller.response;

import com.xfst.simpleblog.service.data.PostDTO;
import lombok.Data;

@Data
public class PostCreateResponse {
    private PostDTO post;
}
