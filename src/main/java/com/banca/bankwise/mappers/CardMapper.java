package com.banca.bankwise.mappers;

import com.banca.bankwise.dtos.CardRequestDTO;
import com.banca.bankwise.dtos.CardResponseDTO;
import com.banca.bankwise.entities.Card;

public class CardMapper {

    public static CardResponseDTO toCardResponseDTO(Card card) {
        if (card == null) {
            return null;
        }
        CardResponseDTO dto = new CardResponseDTO();
        dto.setId(card.getId());
        dto.setCardNumber(card.getCardNumber());
        dto.setCardType(card.getCardType());
        dto.setCircuit(card.getCircuit());
        dto.setActive(card.isActive());
        dto.setExpirationDate(card.getExpirationDate().toString());
        return dto;
    }

    public static Card toEntity(CardRequestDTO cardRequestDTO){
        if(cardRequestDTO == null) {
            return null;
        }
        Card card = new Card();
        card.setCardType(cardRequestDTO.getCardType());
        card.setCircuit(cardRequestDTO.getCircuit());

        return card;
    }
}
