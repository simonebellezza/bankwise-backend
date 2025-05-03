package com.example.BankWise.mappers;

import com.example.BankWise.dtos.TransactionRequestDTO;
import com.example.BankWise.dtos.TransactionResponseDTO;
import com.example.BankWise.dtos.TransactionTransferRequestDTO;
import com.example.BankWise.entities.Account;
import com.example.BankWise.entities.Transaction;
import com.example.BankWise.enums.Currency;
import com.example.BankWise.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionMapper {

    public static Transaction toDeposit(TransactionRequestDTO dto, Account account) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static Transaction toWithdrawal(TransactionRequestDTO dto, Account account) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAccount(account);
        transaction.setAmount(dto.getAmount().negate());
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static Transaction toTransferSender(TransactionTransferRequestDTO dto, Account senderAccount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAccount(senderAccount);
        transaction.setAmount(dto.getAmount().negate());
        transaction.setCurrency(senderAccount.getCurrency());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static Transaction toTransferReceiver(TransactionTransferRequestDTO dto, Account receiverAccount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAccount(receiverAccount);
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(receiverAccount.getCurrency());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }

    public static TransactionResponseDTO toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setTransactionType(transaction.getTransactionType().toString());
        transactionResponseDTO.setAmount(transaction.getAmount());
        transactionResponseDTO.setCurrency(transaction.getCurrency());
        transactionResponseDTO.setDate(transaction.getDate());
        transactionResponseDTO.setDescription(transaction.getDescription());
        return transactionResponseDTO;

    }
}
