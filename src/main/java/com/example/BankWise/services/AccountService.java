package com.example.BankWise.services;

import com.example.BankWise.dtos.AccountDetailsDTO;
import com.example.BankWise.dtos.AccountRequestDTO;
import com.example.BankWise.dtos.AccountDTO;
import com.example.BankWise.entities.Account;
import com.example.BankWise.entities.User;
import com.example.BankWise.exceptions.AccountNotFoundException;
import com.example.BankWise.exceptions.UserNotFoundException;
import com.example.BankWise.mappers.AccountMapper;
import com.example.BankWise.repositories.AccountRepository;
import com.example.BankWise.repositories.UserRepository;
import com.example.BankWise.utils.GenerateIban;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final GenerateIban generateIban;

    public AccountService(UserRepository userRepository,AccountRepository accountRepository, GenerateIban generateIban) {
        this.userRepository = userRepository;
        this.generateIban = generateIban;
        this.accountRepository = accountRepository;
    }

    public AccountDetailsDTO createAccount(AccountRequestDTO accountRequestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = AccountMapper.toAccountEntity(accountRequestDTO);

        // Creazione casuale e univoca dell'iban
        account.setIban(generateIban.generateIban());

        // Sincronizza le relazioni
        account.setUser(user);
        user.getAccounts().add(account);

        // Aggiorno solo User per via del CascadeType.All
        userRepository.save(user);
        return AccountMapper.toAccountDetailsDTO(account);
    }

    public List<AccountDTO> findAll(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        return user.getAccounts().stream()
                .map(AccountMapper::toAccountDTO)
                .collect(Collectors.toList());
    }

    public AccountDetailsDTO findById(String username, long id){
        Account account = accountRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new AccountNotFoundException("Account non trovato o non appartiene all'utente"));
        return AccountMapper.toAccountDetailsDTO(account);
    }
}
