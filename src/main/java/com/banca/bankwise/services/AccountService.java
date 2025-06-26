package com.banca.bankwise.services;

import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.dtos.AccountResponseDTO;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Notification;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.AccountMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.NotificationRepository;
import com.banca.bankwise.repositories.UserRepository;
import com.banca.bankwise.utils.GenerateIban;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final GenerateIban generateIban;

    public AccountService(UserRepository userRepository,
                          AccountRepository accountRepository,
                          NotificationRepository notificationRepository,
                          GenerateIban generateIban) {
        this.userRepository = userRepository;
        this.generateIban = generateIban;
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, String username) {
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

    public List<AccountResponseDTO> findAll(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        return user.getAccounts().stream()
                .map(AccountMapper::toAccountDTO)
                .collect(Collectors.toList());
    }
}
