package com.example.BankWise.dtos;

import com.example.BankWise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDTO {

    private BigDecimal balance;
    private String bankName;
    private Currency currency;
    private String iban;
}
