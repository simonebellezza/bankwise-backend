package com.banca.bankwise.services;

import com.banca.bankwise.dtos.AccountDetailsDTO;
import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.dtos.AccountDTO;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Notification;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.AccountMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.NotificationRepository;
import com.banca.bankwise.repositories.UserRepository;
import com.banca.bankwise.utils.GenerateIban;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    private final GenerateIban generateIban;

    public AccountService(UserRepository userRepository,
                          AccountRepository accountRepository,
                          NotificationRepository notificationRepository,
                          GenerateIban generateIban) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.notificationRepository = notificationRepository;
        this.generateIban = generateIban;
    }

    public AccountDTO createAccount(AccountRequestDTO accountRequestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = AccountMapper.toAccountEntity(accountRequestDTO);

        // Creazione casuale e univoca dell'iban
        account.setIban(generateIban.generateIban());
        account.setUser(user);

        // Creazione della notifica
        Notification notification = new Notification();
        notification.setMessage("Conto creato con successo");
        notification.setUser(user);

        // Salvataggio dell'account e della notifica
        user.getNotifications().add(notification);
        user.getAccounts().add(account);

        // Aggiorno solo User per via del CascadeType.All
        userRepository.save(user);
        return AccountMapper.toAccountDTO(account);
    }

    public List<AccountDTO> findAll(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        return user.getAccounts().stream()
                .map(AccountMapper::toAccountDTO)
                .collect(Collectors.toList());
    }

    public AccountDetailsDTO findById(long id, String username) {
        Account account = accountRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new AccountNotFoundException("Conto non trovato o non appartiene all'utente"));
        return AccountMapper.toAccountDetailsDTO(account);
    }
}
