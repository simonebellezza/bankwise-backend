package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.CardType;
import com.banca.bankwise.enums.Circuit;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class CardRequestDTO {

    @NotNull(message = "Il tipo di carta è obbligatorio")
    private CardType cardType; // es: "DEBIT", "CREDIT"

    @NotNull(message = "Il circuito della carta è obbligatorio")
    private Circuit circuit;  // es: "VISA", "MASTERCARD"

    @NotNull(message = "L'ID account è obbligatorio")
    private long accountId;
}
