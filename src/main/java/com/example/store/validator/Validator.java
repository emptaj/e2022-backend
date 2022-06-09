package com.example.store.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.util.StringUtils;

import com.example.store.exception.ValidationException;

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

    public static void positiveValue(boolean value, String errorMessage) {
        if (value != true)
            throw new ValidationException(errorMessage);
    }

    public static <T> void validate(T dataToValidate){
        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> validationErrors = validator.validate(dataToValidate);
        if(!validationErrors.isEmpty())
            throw new ValidationException(validationErrors.iterator().next().getMessage());
    }
}
