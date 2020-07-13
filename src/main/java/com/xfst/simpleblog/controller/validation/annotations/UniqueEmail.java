package com.xfst.simpleblog.controller.validation.annotations;

import com.xfst.simpleblog.controller.validation.UniqueEmailConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraint.class)
public @interface UniqueEmail {
    String message() default "username already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
