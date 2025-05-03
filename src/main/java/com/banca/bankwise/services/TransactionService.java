package com.banca.bankwise.services;

import com.banca.bankwise.dtos.TransactionRequestDTO;
import com.banca.bankwise.dtos.TransactionResponseDTO;
import com.banca.bankwise.dtos.TransactionTransferRequestDTO;
import com.banca.bankwise.entities.Account;
import com.banca.bankwise.entities.Transaction;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.TransactionMapper;
import com.banca.bankwise.repositories.AccountRepository;
import com.banca.bankwise.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public TransactionService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
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
        Transaction transaction = TransactionMapper.toDeposit(dto, account);

        // Aggiorno il bilancio dell'account e aggiungo la transazione
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        // Aggiungo la transazione alla lista
        account.getTransactions().add(transaction);

        // Salva la transazione (propagazione tramite CascadeType.ALL)
        accountRepository.save(account);

        // Restituisci la risposta mappata
        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto,
                                           String username, long accountId){
        // Recupera l'utente autenticato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        // Recupera l'account e verifica che appartenga all'utente
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
        Transaction transaction = TransactionMapper.toWithdrawal(dto, account);

        // Aggiorno il bilancio dell'account
        account.setBalance(account.getBalance().add(transaction.getAmount()));

        // Aggiungo la transazione alla lista
        account.getTransactions().add(transaction);

        // Persistenza
        accountRepository.save(account);
        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferRequestDTO dto, String username) {

        // Recupero l'utente loggato
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Account non trovato"));

        Account senderAccount = accountRepository.findById(dto.getSenderAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Il tuo account non è stato trovato"));

        Account receiverAccount = accountRepository.findByIban(dto.getReceiverIban())
                .orElseThrow(() -> new AccountNotFoundException("Account del ricevente non trovato"));

        // Verifica che mittente e destinatario non coincidano
        if(senderAccount.getId() == receiverAccount.getId()){
            throw new BadRequestException("Mittente e Destinatario devono essere diversi");
        }

        // Verifica che l'account di chi invia sia dell'utente autenticato
        if (!senderAccount.getUser().equals(sender)) {
            throw new BadRequestException("L'account non appartiene all'utente autenticato");
        }

        // Verifica che l'importo sia consentito
        if (dto.getAmount().compareTo(senderAccount.getBalance()) > 0) {
            throw new BadRequestException("Importo non consentito");
        }


        // Creazione della transazione lato sender
        Transaction senderTransaction = TransactionMapper.toTransferSender(dto, senderAccount);

        // Creazine della transazione lato receiver
        Transaction receiverTransaction = TransactionMapper.toTransferReceiver(dto, receiverAccount);

        // Aggiorniamo i respettivi bilanci
        senderAccount.setBalance(senderAccount.getBalance().add(senderTransaction.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(receiverTransaction.getAmount()));

        // Aggiungiamo le transazioni alle liste
        senderAccount.getTransactions().add(senderTransaction);
        receiverAccount.getTransactions().add(receiverTransaction);

        // Persistenza
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        return TransactionMapper.toDto(senderTransaction);
    }

}
