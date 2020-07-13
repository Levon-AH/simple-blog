package com.xfst.simpleblog.controller.advice;

import com.xfst.simpleblog.constants.ErrorCodes;
import com.xfst.simpleblog.controller.response.ErrorResponse;
import com.xfst.simpleblog.exception.SimpleBlogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimpleBlogException.class)
    public ResponseEntity<ErrorResponse> handle(final SimpleBlogException ex) {
        ErrorCodes errorCodes = ex.getErrorCodes();
        if (errorCodes == null) {
            String message = ex.getMessage() != null ? ex.getMessage() : "something went wrong";
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(message, new Date()));
        }
        ErrorResponse response = new ErrorResponse(errorCodes.getMessage(), new Date());
        return ResponseEntity.status(errorCodes.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(final MethodArgumentNotValidException ex) {
        final Map<String, String> response = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> response.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(final Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage(): "something went wrong";
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message, new Date()));
    }
}
