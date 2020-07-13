package com.xfst.simpleblog.controller.validation;

import com.xfst.simpleblog.controller.validation.annotations.UniqueEmail;
import com.xfst.simpleblog.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;

@RequiredArgsConstructor
public class UniqueEmailConstraint implements ConstraintValidator<UniqueEmail, String> {
    private final UserService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) throws ConstraintViolationException {
        return !service.existsByEmail(value);
    }
}
