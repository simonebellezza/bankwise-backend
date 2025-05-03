package com.example.BankWise.dtos;

import com.example.BankWise.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponseDTO {

    private String transactionType;
    private BigDecimal amount;
    private Currency currency;
    private String description;
    private LocalDate date;
}
