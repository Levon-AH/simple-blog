package com.xfst.simpleblog.controller.response;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    private final String message;
    private final Date time;
}
