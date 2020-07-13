package com.xfst.simpleblog.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public abstract class PostBaseRequest {
    @NotEmpty(message = "title must be not empty")
    protected String title;

    @NotEmpty(message = "text must be not empty")
    protected String text;
}
