package ru.almasgali.raif_test.exception;

import lombok.Getter;

@Getter
public class CurrencyValidationException extends RuntimeException {

    public CurrencyValidationException(String message) {
        super(message);
    }
}
