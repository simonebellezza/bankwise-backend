package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDTO {

    private long id;
    private BigDecimal balance;
    private Currency currency;
    private String iban;
}
