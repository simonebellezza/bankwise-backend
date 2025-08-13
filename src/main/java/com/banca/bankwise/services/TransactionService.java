package com.banca.bankwise.services;

import com.banca.bankwise.dtos.TransactionRequestDTO;
import com.banca.bankwise.dtos.TransactionResponseDTO;
import com.banca.bankwise.dtos.TransactionTransferRequestDTO;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Notification;
import com.banca.bankwise.entities.Transaction;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.TransactionMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.NotificationRepository;
import com.banca.bankwise.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;

    public TransactionService(UserRepository userRepository,
                              AccountRepository accountRepository,
                              NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public TransactionResponseDTO deposit(TransactionRequestDTO dto,
                                                    String username, long accountId) {
        // Recupera l'utente autenticato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        // Recupera l'account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account non trovato"));

        // Verifica che l'account appartenga all'utente
        if (!account.getUser().equals(user)) {
            throw new BadRequestException("L'account non appartiene all'utente autenticato");
        }

        // Crea la transazione
        Transaction transaction = TransactionMapper.toDeposit(dto, account, null);

        // Aggiorno il bilancio dell'account e aggiungo la transazione
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        // Aggiungo la transazione alla lista
        account.getTransactions().add(transaction);

        // Creo la notifica
        Notification notification = new Notification();
        notification.setMessage("Hai effettuato un deposito");
        notification.setUser(user);
        user.getNotifications().add(notification);

        // Persistenza
        accountRepository.save(account);
        notificationRepository.save(notification);

        // Restituisco la risposta mappata
        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto,
                                           String username, long accountId){
        // Recupera l'utente autenticato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        // Recupera l'account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account non trovato"));

        // Verifica che l'account appartenga all'utente
        if (!account.getUser().equals(user)) {
            throw new BadRequestException("L'account non appartiene all'utente autenticato");
        }

        // Verifica che l'importo sia consentito
        if(dto.getAmount().compareTo(account.getBalance()) > 0){
            throw new BadRequestException("Importo non disponibile");
        }

        // Creo la transazione
        Transaction transaction = TransactionMapper.toWithdrawal(dto, account, null);

        // Aggiorno il bilancio dell'account
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        // Aggiungo la transazione alla lista
        account.getTransactions().add(transaction);

        Notification notification = new Notification();
        notification.setMessage("Hai effettuato un prelievo");
        notification.setUser(user);
        user.getNotifications().add(notification);

        // Persistenza
        accountRepository.save(account);
        notificationRepository.save(notification);
        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferRequestDTO dto, String username) {

        // Recupera l'utente autenticato
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Conto non trovato"));

        Account senderAccount = accountRepository.findById(dto.getSenderAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Il tuo account non è stato trovato"));

        Account receiverAccount = accountRepository.findByIban(dto.getReceiverIban())
                .orElseThrow(() -> new AccountNotFoundException("Conto del ricevente non trovato"));

        // Verifica che mittente e destinatario non coincidano
        if(senderAccount.getId() == receiverAccount.getId()){
            throw new BadRequestException("Mittente e Destinatario devono essere diversi");
        }

        // Verifica che l'account di chi invia sia dell'utente autenticato
        if (!senderAccount.getUser().equals(sender)) {
            throw new BadRequestException("Il conto non appartiene all'utente autenticato");
        }

        // Verifica che l'importo sia consentito
        if (dto.getAmount().compareTo(senderAccount.getBalance()) > 0) {
            throw new BadRequestException("Importo non consentito");
        }

        // Creazione della transazione lato sender
        Transaction senderTransaction = TransactionMapper.toTransferSender(dto, senderAccount);

        // Creazione della transazione lato receiver
        Transaction receiverTransaction = TransactionMapper.toTransferReceiver(dto, receiverAccount);

        // Aggiorniamo i rispettivi bilanci
        senderAccount.setBalance(senderAccount.getBalance().add(senderTransaction.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(receiverTransaction.getAmount()));

        // Aggiungiamo le transazioni alle liste
        senderAccount.getTransactions().add(senderTransaction);
        receiverAccount.getTransactions().add(receiverTransaction);

        // Creazione delle notifiche lato sender

        Notification senderNotification = new Notification();
        senderNotification.setMessage("Bonifico disposto a favore di " + receiverAccount.getUser().getFirstName());
        senderNotification.setUser(sender);
        sender.getNotifications().add(senderNotification);

        // Creazione delle notifiche lato receiver

        Notification receiverNotification = new Notification();
        receiverNotification.setMessage("Bonifico ricevuto da " + sender.getFirstName());
        User receiver = receiverAccount.getUser();
        receiverNotification.setUser(receiver);
        receiver.getNotifications().add(receiverNotification);

        // Persistenza
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        notificationRepository.save(senderNotification);
        notificationRepository.save(receiverNotification);

        return TransactionMapper.toDto(senderTransaction);
    }

    public List<TransactionResponseDTO> getTransactions(String username, long accountId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Contro non trovato"));

        // Verifico che il conto appartenga all'utente
        if(!account.getUser().equals(user)) {
            throw new BadRequestException("Il conto non appartiene all'utente autenticato");
        }

        return account.getTransactions().stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }

}
