package ru.almasgali.raif_test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.almasgali.raif_test.data.dto.AccountRequest;
import ru.almasgali.raif_test.data.model.Account;
import ru.almasgali.raif_test.service.AccountService;
import ru.almasgali.raif_test.validation.AccountCurrencyConstraint;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get account by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found account."),
            @ApiResponse(responseCode = "404", description = "Account not found.", content = @Content)
    })
    public Account getAccount(
            @PathVariable
            @Parameter(description = "Id of account.")
            long id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get accounts with given currency.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of accounts."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter.", content = @Content)
    })
    public List<Account> getAccountsByCurrency(
            @RequestParam
            @AccountCurrencyConstraint
            @Valid
            @Parameter(description = "Either RUB, EUR or USD.")
            String currency) {
        return accountService.getByCurrency(currency);
    }

    @PutMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Increase balance of specified account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated account"),
            @ApiResponse(responseCode = "404", description = "Account not found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid parameter.", content = @Content)
    })
    public Account deposit(
            @PathVariable
            @Parameter(description = "Id of account.")
            long id,
            @RequestParam
            @Min(0)
            @Parameter(description = "Increase amount.")
            double amount) {
        return accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Decrease balance of specified account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated account"),
            @ApiResponse(responseCode = "404", description = "Account not found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid parameter.", content = @Content)
    })
    public Account withdraw(
            @PathVariable
            @Parameter(description = "Id of account.")
            long id,
            @RequestParam
            @Min(0)
            @Parameter(description = "Decrease amount.")
            double amount) {
        return accountService.withdraw(id, amount);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter.", content = @Content)
    })
    public Account createAccount(
            @RequestBody
            @Valid
            @Parameter(description = "Parameters of new account.")
            AccountRequest request) {
        return accountService.createAccount(request.getCurrency());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Delete account.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Deleted if present, else nothing happened."),
    })
    public void closeAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
    }
}
