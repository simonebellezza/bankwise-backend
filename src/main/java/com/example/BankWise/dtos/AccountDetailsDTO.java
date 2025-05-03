package com.example.BankWise.dtos;

import com.example.BankWise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDetailsDTO {

    private BigDecimal balance;
    private String bankName;
    private Currency currency;
    private String iban;
    private List<TransactionResponseDTO> transactions;
}
