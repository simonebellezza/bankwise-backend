package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.CardRequestDTO;
import com.banca.bankwise.dtos.CardResponseDTO;
import com.banca.bankwise.dtos.TransactionRequestDTO;
import com.banca.bankwise.dtos.TransactionResponseDTO;
import com.banca.bankwise.services.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Crea una nuova carta")
    @PostMapping("/create")
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CardRequestDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CardResponseDTO cardResponseDTO = cardService.createCard(username, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseDTO);
    }

    @Operation(summary = "Effettua un pagamento con una carta")
    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDTO> paymentByCard(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = cardService.paymentByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Ricarica una carta")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> depositByCard(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = cardService.depositByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Preleva denaro da una carta")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdrawByCard(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDTO transactionResponseDTO = cardService.withdrawalByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }



}
