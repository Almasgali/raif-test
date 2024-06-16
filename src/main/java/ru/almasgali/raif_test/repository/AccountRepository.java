package ru.almasgali.raif_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.almasgali.raif_test.data.model.Account;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCurrency(String currency);

    List<Account> findByBalanceGreaterThan(double balance);
}
