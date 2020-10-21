package com.blocker147.vftask.exception;

import com.blocker147.vftask.animal.AnimalNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @Autowired
    private ExceptionHandlerResponse exceptionHandler;

    @ExceptionHandler(AnimalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> animalNotFoundException(AnimalNotFoundException exception) {
        log.error(exception.getClass().getSimpleName() + ". " + exception.getMessage());
        return exceptionHandler.setResponseWithErrors(HttpStatus.NOT_FOUND, Collections.singletonList(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " : " + error.getDefaultMessage());
        }
        log.error(exception.getClass().getSimpleName() + ". " + errors.toString());
        return exceptionHandler.setResponseWithErrors(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> nullPointerException(Exception exception) {
        log.error(exception.getClass().getSimpleName() + ". " + exception.getMessage());
        return exceptionHandler.setResponseWithErrors(HttpStatus.NOT_FOUND, Collections.singletonList(exception.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> noHandlerException(NoHandlerFoundException exception) {
        log.error(exception.getClass().getSimpleName() + ". " + exception.getMessage());
        return exceptionHandler.setResponseWithErrors(HttpStatus.NOT_FOUND, Collections.singletonList(exception.getMessage()));
    }
}
