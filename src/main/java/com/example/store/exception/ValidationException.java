package com.example.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ValidationException extends HttpStatusCodeException {
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
