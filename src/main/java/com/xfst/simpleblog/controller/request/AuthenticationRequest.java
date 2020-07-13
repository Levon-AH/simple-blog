package com.xfst.simpleblog.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequest {
    @NotNull
    @Size(min = 5, message = "size of username must be higher or equal to {min}")
    private String username;

    @NotNull
    @Size(min = 5, message = "size of password must be higher or equal to {min}")
    private String password;
}
