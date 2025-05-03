package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDTO {

    private BigDecimal balance;
    private String bankName;
    private Currency currency;
    private String iban;
}
