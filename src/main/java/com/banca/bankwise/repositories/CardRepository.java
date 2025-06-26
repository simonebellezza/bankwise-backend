package com.banca.bankwise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.banca.bankwise.entities.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);
}
