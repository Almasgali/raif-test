package ru.almasgali.raif_test.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;

@Getter
public class CurrencyValidationException extends RuntimeException {

    public CurrencyValidationException(String message) {
        super(message);
    }
}
