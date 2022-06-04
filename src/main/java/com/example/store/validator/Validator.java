package com.example.store.validator;

import javax.validation.ValidationException;

import org.springframework.util.StringUtils;

public class Validator {
    public static void stringNotEmpty(String string, String errorMessage) {
        if (!StringUtils.hasText(string))
            throw new ValidationException(errorMessage);
    }

    public static <T> void notNull(T value, String errorMessage) {
        if (value == null)
            throw new ValidationException(errorMessage);
    }

    public static void positiveValue(Float value, String errorMessage) {
        if (value <= 0)
            throw new ValidationException(errorMessage);
    }

    public static void positiveValue(Integer value, String errorMessage) {
        if (value <= 0)
            throw new ValidationException(errorMessage);
    }

    public static void notNegative(Integer value, String errorMessage) {
        if (value < 0)
            throw new ValidationException(errorMessage);
    }
}
