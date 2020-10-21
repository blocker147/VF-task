package com.blocker147.vftask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExceptionHandlerResponse {
    public Map<String, Object> setResponseWithErrors(HttpStatus status, List<String> errors) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", new Date());
        response.put("status", status);
        response.put("errors", errors);
        return response;
    }
}
