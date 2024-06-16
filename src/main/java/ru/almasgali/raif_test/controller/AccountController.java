package ru.almasgali.raif_test.controller;

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
    public Account getAccount(@PathVariable long id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccountsByCurrency(@RequestParam @AccountCurrencyConstraint @Valid String currency) {
        return accountService.getByCurrency(currency);
    }

    @PutMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public Account deposit(@PathVariable long id, @RequestParam @Min(0) double amount) {
        return accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public Account withdraw(@PathVariable long id, @RequestParam @Min(0) double amount) {
        return accountService.withdraw(id, amount);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Valid AccountRequest request) {
        return accountService.createAccount(request.getCurrency());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void closeAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
    }
}
