package ru.almasgali.raif_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.almasgali.raif_test.data.model.Account;
import ru.almasgali.raif_test.exception.AccountNotFoundException;
import ru.almasgali.raif_test.exception.NotEnoughMoneyException;
import ru.almasgali.raif_test.repository.AccountRepository;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account deposit(long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id " + id + " not found."));

        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    public Account withdraw(long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id " + id + " not found."));

        double currentBalance = account.getBalance();
        if (currentBalance < amount) {
            throw new NotEnoughMoneyException("Balance: " + currentBalance + ", withdraw: " + amount);
        }
        account.setBalance(currentBalance - amount);
        return accountRepository.save(account);
    }

    public Account getAccount(long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id " + id + " not found."));
    }

    public Account createAccount(String currency) {
        Account account = Account.builder().currency(currency).build();
        return accountRepository.save(account);
    }

    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }

    public List<Account> getByCurrency(String currency) {
        return accountRepository.findByCurrency(currency);
    }
}
