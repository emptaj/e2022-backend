package com.example.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NotFoundException extends HttpStatusCodeException {

    public NotFoundException(Class<?> clazz, Long id) {
        super(
            HttpStatus.NOT_FOUND,
            String.format(
                "%s with id = %d not found",
                clazz.getSimpleName().replace("Entity", ""),
                id
            )
        );
    }
}
