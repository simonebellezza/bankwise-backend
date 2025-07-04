package com.banca.bankwise.mappers;

import com.banca.bankwise.dtos.AccountResponseDTO;
import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.entities.Account;

public class AccountMapper {

    public static AccountResponseDTO toAccountDTO(Account account){
        AccountResponseDTO accountDTO = new AccountResponseDTO();
        accountDTO.setId(account.getId());
        accountDTO.setIban(account.getIban());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCurrency(account.getCurrency());
        accountDTO.setAccountNumber(account.getAccountNumber());
        return accountDTO;
    }

    public static Account toAccountEntity(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        account.setBalance(accountRequestDTO.getBalance());
        account.setCurrency(accountRequestDTO.getCurrency());
        return account;
    }
}
