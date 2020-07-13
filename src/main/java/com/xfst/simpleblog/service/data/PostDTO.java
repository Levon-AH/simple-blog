package com.xfst.simpleblog.service.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String text;
    private Date createdTime;
    private Date updatedTime;
    private Date deletedTime;
}
