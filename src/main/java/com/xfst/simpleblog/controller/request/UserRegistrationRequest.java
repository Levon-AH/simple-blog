package com.xfst.simpleblog.controller.request;

import com.xfst.simpleblog.controller.validation.annotations.UniqueEmail;
import com.xfst.simpleblog.controller.validation.annotations.UniqueUsername;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationRequest {
    @NotNull
    @Size(min = 3, max = 15, message = "please input first name between {min} to {max}")
    private String firstName;

    @NotNull
    @Size(max = 30, message = "please input last name between {min} to {max}")
    private String lastName;

    @NotNull
    @Size(min = 5, message = "size of username must be higher or equal to {min}")
    @UniqueUsername
    private String username;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "please input valid email")
    @UniqueEmail
    @Size(max = 100, message = "size of email must be lower or equal to {max}")
    private String email;

    @NotNull
    @Size(min = 5, message = "size of password must be higher or equal to {min}")
    private String password;
}
