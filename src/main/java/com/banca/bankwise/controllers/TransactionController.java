package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.TransactionRequestDTO;
import com.banca.bankwise.dtos.TransactionResponseDTO;
import com.banca.bankwise.dtos.TransactionTransferRequestDTO;
import com.banca.bankwise.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Deposita denaro")
    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long accountId, Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.deposit(transactionRequestDTO, username, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Preleva denaro")
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long accountId, Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.withdraw(transactionRequestDTO, username, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Trasferisci denaro")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionTransferRequestDTO transactionTransferRequestDTO, Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.transfer(transactionTransferRequestDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Recupera le transazioni dell'account per id")
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable long accountId, Principal principal) {
        String username = principal.getName();
        List<TransactionResponseDTO> transactions = transactionService.getTransactions(username, accountId);
        return ResponseEntity.ok(transactions);
    }

}
