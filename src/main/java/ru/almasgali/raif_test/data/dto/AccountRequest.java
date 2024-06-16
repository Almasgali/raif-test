package ru.almasgali.raif_test.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.almasgali.raif_test.validation.AccountCurrencyConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    @AccountCurrencyConstraint
    @Schema(name = "Account currency - either RUB, EUR or USD.", example = "RUB")
    private String currency;
}

