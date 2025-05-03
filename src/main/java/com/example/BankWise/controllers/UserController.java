package com.example.BankWise.controllers;

import com.example.BankWise.dtos.UserLoginDTO;
import com.example.BankWise.dtos.UserRegisterDTO;
import com.example.BankWise.dtos.UserResponseDTO;
import com.example.BankWise.services.AccountService;
import com.example.BankWise.services.UserService;
import com.example.BankWise.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public UserController(AccountService accountService,UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Registra un nuovo utente")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO user) {
        UserResponseDTO userResponseDTO = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato con successo: " + userResponseDTO.getFirstName() + " " + userResponseDTO.getLastName());
    }

    @Operation(summary = "Login utente")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok("bearer token: " + token);
    }

    @Operation(summary = "Informazioni utente corrente con i relativi accounts")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponseDTO);
    }

}
