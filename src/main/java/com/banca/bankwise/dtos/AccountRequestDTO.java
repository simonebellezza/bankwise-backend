package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {

    @Min(value = 0, message = "Il saldo deve essere maggiore o uguale a 0.00")
    private BigDecimal balance;

    @NotNull(message = "La valuta è obbligatoria.")
    private Currency currency;
}