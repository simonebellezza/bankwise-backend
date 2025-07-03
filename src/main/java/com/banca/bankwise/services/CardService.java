package com.banca.bankwise.services;

import com.banca.bankwise.dtos.*;
import com.banca.bankwise.entities.*;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.CardNotFoundException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.CardMapper;
import com.banca.bankwise.mappers.TransactionMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.CardRepository;
import com.banca.bankwise.repositories.NotificationRepository;
import com.banca.bankwise.repositories.UserRepository;
import com.banca.bankwise.utils.GenerateCard;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final GenerateCard generateCard;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public CardService(UserRepository userRepository,
                       CardRepository cardRepository,
                       AccountRepository accountRepository,
                       NotificationRepository notificationRepository,
                       GenerateCard generateCard,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.notificationRepository = notificationRepository;
        this.generateCard = generateCard;
        this.passwordEncoder = passwordEncoder;
    }

    public CardResponseDTO createCard(String username, CardRequestDTO cardRequestDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = accountRepository.findById(cardRequestDTO.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Conto non trovato"));

        // Controllo sull'appartenenza dell'account all'utente
        if (!account.getUser().getUsername().equals(username)) {
            throw new UserNotFoundException("Il conto non appartiene all'utente");
        }

        // Creiamo la carta
        Card card = CardMapper.toEntity(cardRequestDTO);
        card.setUser(user);
        card.setAccount(account);
        card.setCardNumber(generateCard.generateCardNumber(cardRequestDTO.getCircuit()));
        String pin = generateCard.generatePin();
        card.setPin(passwordEncoder.encode(pin));
        card.setIban(account.getIban());

        // Creiamo la notifica
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("La tua carta numero: " + card.getCardNumber() + " adesso è attiva!");

        // Salviamo la carta e la notifica
        cardRepository.save(card);
        notificationRepository.save(notification);
        CardResponseDTO cardResponseDTO = CardMapper.toCardResponseDTO(card);
        cardResponseDTO.setPin(pin);
        return cardResponseDTO;
    }

    @Transactional
    public TransactionResponseDTO depositByCard(String username, TransactionRequestByCard transactionRequestDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Card card = cardRepository.findById(transactionRequestDTO.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Carta non trovata"));
        Account account = card.getAccount();

        // Controllo che il conto appartenga all'utente
        if (!account.getUser().getUsername().equals(username)) {
            throw new BadRequestException("Il conto non appartiene all'utente");
        }

        // Controllo che la carta appartenga all'utente
        if (!card.getUser().getUsername().equals(username)) {
            throw new BadRequestException("La carta non appartiene all'utente");
        }

        // Controllo che il PIN sia corretto
        if (!passwordEncoder.matches(transactionRequestDTO.getPin(), card.getPin())) {
            throw new BadRequestException("PIN errato");
        }

        Transaction transaction = CardMapper.toDeposit(transactionRequestDTO, account, card);

        // Aggiorno il bilancio dell'account e aggiungo la transazione
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        // Aggiungo la transazione alla lista
        account.getTransactions().add(transaction);

        // Creo la notifica
        Notification notification = new Notification();
        String cardNumber = card.getCardNumber().substring(card.getCardNumber().length() - 4);

        notification.setMessage("Deposito ATM sul conto: " + cardNumber);
        notification.setUser(user);
        user.getNotifications().add(notification);

        // Persistenza
        accountRepository.save(account);
        notificationRepository.save(notification);

        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO paymentByCard(String username, TransactionRequestByCard transactionRequestDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Card card = cardRepository.findById(transactionRequestDTO.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Carta non trovata"));
        Account account = card.getAccount();

        // Controllo che il conto appartenga all'utente
        if (!account.getUser().getUsername().equals(username)) {
            throw new BadRequestException("Il conto non appartiene all'utente");
        }

        // Controllo che la carta appartenga all'utente
        if (!card.getUser().getUsername().equals(username)) {
            throw new BadRequestException("La carta non appartiene all'utente");
        }

        // Controllo che il pin sia corretto
        if (!passwordEncoder.matches(transactionRequestDTO.getPin(), card.getPin())) {
            throw new BadRequestException("PIN errato");
        }

        // Controllo saldo sufficiente per il pagamento
        if (account.getBalance().compareTo(transactionRequestDTO.getAmount()) < 0) {
            throw new BadRequestException("Saldo insufficiente per effettuare il pagamento");
        }

        // Creo la transazione di pagamento (importo negativo)
        Transaction transaction = CardMapper.toPayment(transactionRequestDTO, account, card);

        // Aggiorno il bilancio sottraendo l'importo
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        account.getTransactions().add(transaction);

        // Notifica
        Notification notification = new Notification();
        String cardNumber = card.getCardNumber().substring(card.getCardNumber().length() - 4);
        notification.setMessage("Pagamento effettuato tramite carta: " + cardNumber);
        notification.setUser(user);
        user.getNotifications().add(notification);

        accountRepository.save(account);
        notificationRepository.save(notification);

        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO withdrawalByCard(String username, TransactionRequestByCard transactionRequestDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Card card = cardRepository.findById(transactionRequestDTO.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Carta non trovata"));
        Account account = card.getAccount();

        // Controllo che il conto appartenga all'utente
        if (!account.getUser().getUsername().equals(username)) {
            throw new BadRequestException("Il conto non appartiene all'utente");
        }

        // Controllo che la carta appartenga all'utente
        if (!card.getUser().getUsername().equals(username)) {
            throw new BadRequestException("La carta non appartiene all'utente");
        }

        // Controllo che il pin sia corretto
        if (!passwordEncoder.matches(transactionRequestDTO.getPin(), card.getPin())) {
            throw new BadRequestException("PIN errato");
        }

        // Controllo saldo sufficiente per il prelievo
        if (account.getBalance().compareTo(transactionRequestDTO.getAmount()) < 0) {
            throw new BadRequestException("Saldo insufficiente per effettuare il prelievo");
        }

        // Creo la transazione di prelievo (importo negativo)
        Transaction transaction = CardMapper.toWithdrawal(transactionRequestDTO, account, card);

        // Aggiorno il bilancio del conto
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        account.getTransactions().add(transaction);

        // Notifica
        Notification notification = new Notification();
        String cardNumber = card.getCardNumber().substring(card.getCardNumber().length() - 4);
        notification.setMessage("Prelievo ATM tramite carta: " + cardNumber);
        notification.setUser(user);
        user.getNotifications().add(notification);

        accountRepository.save(account);
        notificationRepository.save(notification);

        return TransactionMapper.toDto(transaction);
    }

    public List<CardResponseDTO> getCardsByAccountId(String username, Long accountId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Conto non trovato"));

        // Controllo sull'appartenenza dell'account all'utente
        if (!account.getUser().getUsername().equals(username)) {
            throw new UserNotFoundException("Il conto non appartiene all'utente");
        }

        return account.getCards().stream()
                .map(CardMapper::toCardResponseDTO)
                .toList();
    }

}
