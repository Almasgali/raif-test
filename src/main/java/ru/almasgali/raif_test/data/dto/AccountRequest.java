package ru.almasgali.raif_test.data.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.almasgali.raif_test.validation.AccountCurrencyConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    @AccountCurrencyConstraint
    private String currency;
}

