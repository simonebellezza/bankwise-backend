package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDetailsDTO {

    private BigDecimal balance;
    private Currency currency;
    private String iban;
    private List<TransactionResponseDTO> transactions;
}
