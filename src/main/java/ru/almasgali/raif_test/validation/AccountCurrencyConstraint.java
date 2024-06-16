package ru.almasgali.raif_test.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountCurrencyConstraintValidator.class)
public @interface AccountCurrencyConstraint {
    String message() default "Currency should be either RUB, EUR or USD.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
