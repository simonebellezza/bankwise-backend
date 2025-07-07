package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.UserLoginDTO;
import com.banca.bankwise.dtos.UserRegisterDTO;
import com.banca.bankwise.dtos.UserResponseDTO;
import com.banca.bankwise.dtos.UserUpdateDTO;
import com.banca.bankwise.services.AccountService;
import com.banca.bankwise.services.UserService;
import com.banca.bankwise.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Registra un nuovo utente")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO user) {
        UserResponseDTO userResponseDTO = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato correttamente");
    }

    @Operation(summary = "Login utente")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @Operation(summary = "Informazioni utente corrente con i relativi accounts")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        String username = principal.getName();
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Aggiorna le informazioni dell'utente")
    @PatchMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO user, Principal principal) {
        String username = principal.getName();
        UserResponseDTO updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Elimina l'utente corrente")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Principal principal) {
        String username = principal.getName();
        userService.deleteUser(username);
        return ResponseEntity.ok("Utente eliminato correttamente");
    }

}
