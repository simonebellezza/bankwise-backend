package com.example.BankWise.controllers;

import com.example.BankWise.dtos.TransactionRequestDTO;
import com.example.BankWise.dtos.TransactionResponseDTO;
import com.example.BankWise.dtos.TransactionTransferRequestDTO;
import com.example.BankWise.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Deposita denaro")
    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long accountId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.deposit(transactionRequestDTO, username, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Preleva denaro")
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO, @PathVariable long accountId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.withdraw(transactionRequestDTO, username, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Trasferisci denaro")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionTransferRequestDTO transactionTransferRequestDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = transactionService.transfer(transactionTransferRequestDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }







}
