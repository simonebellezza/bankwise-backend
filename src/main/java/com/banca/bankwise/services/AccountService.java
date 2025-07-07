package com.banca.bankwise.services;

import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.dtos.AccountResponseDTO;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Notification;
import com.banca.bankwise.entities.Transaction;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.enums.TransactionType;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.AccountMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.NotificationRepository;
import com.banca.bankwise.repositories.UserRepository;
import com.banca.bankwise.utils.GenerateNumbers;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final GenerateNumbers generateNumbers;
    private final AccountRepository accountRepository;

    public AccountService(UserRepository userRepository,
                          AccountRepository accountRepository,
                          NotificationRepository notificationRepository,
                          GenerateNumbers generateNumbers) {
        this.userRepository = userRepository;
        this.generateNumbers = generateNumbers;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = AccountMapper.toAccountEntity(accountRequestDTO);

        // Creazione casuale e univoca dell'iban
        account.setIban(generateNumbers.generateIban());
        account.setUser(user);

        // Creazione casuale del numero di conto corrente
        account.setAccountNumber(generateNumbers.generateAccountNumber());

        // Creazione della transazione iniziale
        Transaction transaction = new Transaction();
        transaction.setCurrency(accountRequestDTO.getCurrency());
        transaction.setAccount(account);
        transaction.setAmount(accountRequestDTO.getBalance());
        transaction.setDescription("Deposito iniziale");
        transaction.setTransactionType(TransactionType.DEPOSIT);

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

    public AccountResponseDTO findById(String username, long id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Conto non esistente"));

        if (!account.getUser().getUsername().equals(username)){
            throw new BadRequestException("Il conto non appartiene all'utente autenticato");
        }

        return AccountMapper.toAccountDTO(account);
    }

    @Transactional
    public void deleteAccount(String username, long id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Conto non esistente"));

        if (!account.getUser().getUsername().equals(username)){
            throw new BadRequestException("Il conto non appartiene all'utente autenticato");
        }

        // Rimuovo l'account dall'utente
        user.getAccounts().remove(account);
        accountRepository.delete(account);
    }
}
