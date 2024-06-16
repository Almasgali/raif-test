package ru.almasgali.raif_test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import ru.almasgali.raif_test.exception.AccountNotFoundException;
import ru.almasgali.raif_test.exception.CurrencyValidationException;

@Component
public class AccountCurrencyConstraintValidator implements ConstraintValidator<AccountCurrencyConstraint, String> {

    private String errorMessage;

    @Override
    public void initialize(AccountCurrencyConstraint constraintAnnotation) {
        this.errorMessage = constraintAnnotation.message();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        boolean valid = s != null && (s.equals("RUB") || s.equals("EUR") || s.equals("USD"));

        if (!valid) {
            throw new CurrencyValidationException(errorMessage);
        }

        return true;
    }
}
