package com.example.BankWise.dtos;

import com.example.BankWise.enums.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {

    @Min(value = 0, message = "Il saldo deve essere maggiore o uguale a 0.00")
    private BigDecimal balance;

    @NotBlank(message = "Il nome della banca è obbligatorio.")
    private String bankName;

    @NotNull(message = "La valuta è obbligatoria.")
    private Currency currency;
}