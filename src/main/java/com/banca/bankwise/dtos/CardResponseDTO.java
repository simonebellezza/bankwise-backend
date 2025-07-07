package com.banca.bankwise.dtos;

import com.banca.bankwise.enums.CardType;
import com.banca.bankwise.enums.Circuit;
import lombok.Data;

@Data
public class CardResponseDTO {

    private long id;
    private String cardNumber;
    private CardType cardType;
    private Circuit circuit;
    private String expirationDate;
    private boolean active;
    private String pin;
}
