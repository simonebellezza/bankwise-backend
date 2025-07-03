package com.banca.bankwise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestByCard {

    @Positive(message = "L'importo deve essere positivo.")
    private BigDecimal amount;

    @Size(max = 255, message = "La descrizione non può superare i 255 caratteri.")
    private String description;

    @NotNull(message = "L'ID della carta è obbligatorio.")
    private Long cardId;

    @NotBlank(message = "Il PIN è obbligatorio.")
    private String pin;
}
