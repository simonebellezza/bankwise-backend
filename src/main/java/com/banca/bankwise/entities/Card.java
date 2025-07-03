package com.banca.bankwise.entities;

import com.banca.bankwise.enums.CardType;
import com.banca.bankwise.enums.Circuit;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "iban", nullable = false, unique = true)
    private String iban;

    @Column(name = "card_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType cardType; // es: "DEBIT", "CREDIT"

    @Column(name = "circuit", nullable = false)
    @Enumerated(EnumType.STRING)
    private Circuit circuit;  // es: "VISA", "MASTERCARD"

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate = LocalDate.now().plusYears(3);

    @Column(name = "pin", nullable = false)
    private String pin;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}