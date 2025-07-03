package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.AccountResponseDTO;
import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @Operation(summary = "Crea un nuovo account")
    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO, Principal principal) {
        String username = principal.getName();
        AccountResponseDTO accountResponseDTO = accountService.createAccount(accountRequestDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponseDTO);
    }

    @Operation(summary = "Recupera i conti dell'utente corrente")
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponseDTO>> findAllAccounts(Principal principal) {
        String username = principal.getName();
        List<AccountResponseDTO> accounts = accountService.findAll(username);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Recupera il conto dell'utente corrente")
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> findAccountById(Principal principal,@PathVariable long id) {
        String username = principal.getName();
        AccountResponseDTO account = accountService.findById(username, id);
        return ResponseEntity.ok(account);
    }
}
