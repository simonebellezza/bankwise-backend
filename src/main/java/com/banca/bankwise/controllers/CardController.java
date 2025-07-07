package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.*;
import com.banca.bankwise.services.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Crea una nuova carta")
    @PostMapping("/create")
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CardRequestDTO dto, Principal principal) {
        String username = principal.getName();
        CardResponseDTO cardResponseDTO = cardService.createCard(username, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseDTO);
    }

    @Operation(summary = "Effettua un pagamento tramite carta")
    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDTO> paymentByCard(@Valid @RequestBody TransactionRequestByCard transactionRequestDTO, Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = cardService.paymentByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Deposita denaro tramite carta")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> depositByCard(@Valid @RequestBody TransactionRequestByCard transactionRequestDTO,Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = cardService.depositByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Preleva denaro tramite carta")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdrawByCard(@Valid @RequestBody TransactionRequestByCard transactionRequestDTO,Principal principal) {
        String username = principal.getName();
        TransactionResponseDTO transactionResponseDTO = cardService.withdrawalByCard(username, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO);
    }

    @Operation(summary = "Recupera le carte di un conto corrente")
    @GetMapping("/{accountId}")
    public ResponseEntity<List<CardResponseDTO>> getCardsByAccountId(@PathVariable Long accountId, Principal principal) {
        String username = principal.getName();
        List<CardResponseDTO> cardResponseDTOs = cardService.getCardsByAccountId(username, accountId);
        return ResponseEntity.ok(cardResponseDTOs);
    }

    @Operation(summary = "Recupera le carte di un utente")
    @GetMapping("/cards")
    public ResponseEntity<List<CardResponseDTO>> getCardsByUser(Principal principal) {
        String username = principal.getName();
        List<CardResponseDTO> cardResponseDTOs = cardService.getCardsByUser(username);
        return ResponseEntity.ok(cardResponseDTOs);
    }

    @Operation(summary = "Elimina una carta")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        cardService.deleteCard(username, id);
        return ResponseEntity.noContent().build();
    }

}
