package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.AccountDTO;
import com.banca.bankwise.dtos.AccountDetailsDTO;
import com.banca.bankwise.dtos.AccountRequestDTO;
import com.banca.bankwise.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccountDetailsDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountDetailsDTO accountResponseDTO = accountService.createAccount(accountRequestDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponseDTO);
    }

    @Operation(summary = "Recupera accounts dell'utente corrente")
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> findAllAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AccountDTO> accounts = accountService.findAll(username);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Recupera un account dell'utente con la lista di movimenti")
    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailsDTO> findAccountById(@PathVariable long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountDetailsDTO accountDetailsDTO = accountService.findById(username, id);
        return ResponseEntity.ok(accountDetailsDTO);
    }
}
