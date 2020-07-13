package com.xfst.simpleblog.controller.validation;

import com.xfst.simpleblog.controller.validation.annotations.UniqueUsername;
import com.xfst.simpleblog.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueUsernameConstraint implements ConstraintValidator<UniqueUsername, String> {
    private final UserService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !service.existsByUsername(value);
    }
}
