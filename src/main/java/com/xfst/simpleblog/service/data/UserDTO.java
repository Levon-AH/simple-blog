package com.xfst.simpleblog.service.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private List<PostDTO> posts;
    private boolean active;
    private Date createdTime;
}
