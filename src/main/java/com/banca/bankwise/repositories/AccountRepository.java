package com.banca.bankwise.repositories;

import com.banca.bankwise.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByIban(String iban);
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByIban(String Iban);
;
}
