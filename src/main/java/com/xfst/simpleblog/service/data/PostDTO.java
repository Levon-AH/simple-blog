package com.xfst.simpleblog.service.data;

import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String text;
    private Date createdTime;
    private Date updatedTime;
    private Date deletedTime;
}
