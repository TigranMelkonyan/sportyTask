package com.sporty.iam.controller;


import com.sporty.iam.controller.model.response.error.ErrorResponse;
import com.sporty.iam.domain.model.common.exception.ErrorCode;
import com.sporty.iam.domain.model.common.exception.RecordConflictException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tigran Melkonyan
 * Date: 2/19/25
 * Time: 1:02 PM
 */
@RestControllerAdvice
public class AbstractController {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RecordConflictException.class)
    @ResponseBody
    protected final ErrorResponse handle(final RecordConflictException e) {
        return new ErrorResponse(ErrorCode.RECORD_CONFLICT, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    protected final ErrorResponse handle(final IllegalArgumentException e) {
        return new ErrorResponse(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    protected final ErrorResponse handle(final ConstraintViolationException e) {
        return new ErrorResponse(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return errors;
    }
}
