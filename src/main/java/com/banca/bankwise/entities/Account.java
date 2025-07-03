package com.banca.bankwise.entities;

    import com.banca.bankwise.enums.Currency;
    import jakarta.persistence.*;
    import lombok.*;

    import java.math.BigDecimal;
    import java.util.List;

    @Entity
    @Table(name = "accounts")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "balance", nullable = false)
        private BigDecimal balance;

        @Column(name = "currency", nullable = false)
        @Enumerated(EnumType.STRING)
        private Currency currency;

        @Column(name = "iban", nullable = false, unique = true)
        private String iban;

        @Column(name = "account_number", nullable = false, unique = true)
        private String accountNumber;

        // Relazioni

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Transaction> transactions;

        @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Card> cards;



    }