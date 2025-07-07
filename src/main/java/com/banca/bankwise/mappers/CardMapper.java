package com.banca.bankwise.mappers;

import com.banca.bankwise.dtos.*;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Card;
import com.banca.bankwise.entities.Transaction;
import com.banca.bankwise.enums.TransactionType;

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

    public static Transaction toDeposit(TransactionRequestByCard dto, Account account, Card card) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription(dto.getDescription());
        if (card != null) {
            transaction.setCard(card);
        }
        return transaction;
    }

    public static Transaction toWithdrawal(TransactionRequestByCard dto, Account account, Card card) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount().negate());
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription(dto.getDescription());
        if (card != null) {
            transaction.setCard(card);
        }
        return transaction;
    }
    public static Transaction toPayment(TransactionRequestByCard dto, Account account, Card card) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount().negate());
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription(dto.getDescription());
        if (card != null) {
            transaction.setCard(card);
        }
        return transaction;
    }
}
