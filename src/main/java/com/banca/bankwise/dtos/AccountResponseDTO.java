package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountResponseDTO {

    private long id;
    private BigDecimal balance;
    private Currency currency;
    private String iban;
    private String accountNumber;
}
