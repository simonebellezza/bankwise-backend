package com.example.BankWise.mappers;

import com.example.BankWise.dtos.AccountDTO;
import com.example.BankWise.dtos.AccountDetailsDTO;
import com.example.BankWise.dtos.AccountRequestDTO;
import com.example.BankWise.entities.Account;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDetailsDTO toAccountDetailsDTO(Account account) {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setCurrency(account.getCurrency());
        accountDetailsDTO.setIban(account.getIban());
        accountDetailsDTO.setBalance(account.getBalance());
        accountDetailsDTO.setBankName(account.getBankName());

        // Lista transazioni se ci sono
        if (account.getTransactions() != null){
            accountDetailsDTO.setTransactions(account.getTransactions().stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList()));
        }
        return accountDetailsDTO;
    }

    public static AccountDTO toAccountDTO(Account account){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBankName(account.getBankName());
        accountDTO.setIban(account.getIban());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCurrency(account.getCurrency());
        return accountDTO;
    }

    public static Account toAccountEntity(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        account.setBalance(accountRequestDTO.getBalance());
        account.setBankName(accountRequestDTO.getBankName());
        account.setCurrency(accountRequestDTO.getCurrency());
        return account;
    }
}
