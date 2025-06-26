package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponseDTO {

    private long id;
    private String transactionType;
    private BigDecimal amount;
    private Currency currency;
    private String description;
    private LocalDate date;
}
