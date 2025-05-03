package com.example.BankWise.repositories;

import com.example.BankWise.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByIban(String iban);

    Optional<Account> findByIdAndUserUsername(long id, String username);
    Optional<Account> findByIban(String Iban);
}
