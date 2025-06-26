package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.ErrorResponseDTO;
import com.banca.bankwise.exceptions.AccountNotFoundException;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {

    // Gestisce Username non presenti
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, ex.getMessage()));
    }

    // Gestisce accounts non presenti
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, ex.getMessage()));
    }

    // Gestisce generiche Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(400, ex.getMessage()));
    }

    // Gestisce errori di validazione dei controllers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    // Gestisce errori di vincolo d'integrità
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(409, "Vincolo di integrità violato: dati duplicati o invalidi"));
    }

    // Gestisce valori enum non presenti. Currency è l'unico enum inserito lato client
   // Gestisce valori enum non presenti per CardType e Circuit
   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity<ErrorResponseDTO> handleInvalidEnum(HttpMessageNotReadableException ex) {
       String message = ex.getMessage();
       if (message.contains("Currency")) {
           return ResponseEntity.badRequest().body(
               new ErrorResponseDTO(400, "Valore non valido per il campo 'currency'. Valori ammessi: EUR, USD, GBP, INR, JPY.")
           );
       }
       if (message.contains("CardType")) {
           return ResponseEntity.badRequest().body(
               new ErrorResponseDTO(400, "Valore non valido per il campo 'cardType'. Valori ammessi: DEBIT, CREDIT.")
           );
       }
       if (message.contains("Circuit")) {
           return ResponseEntity.badRequest().body(
               new ErrorResponseDTO(400, "Valore non valido per il campo 'circuit'. Valori ammessi: VISA, MASTERCARD, AMERICAN_EXPRESS.")
           );
       }
       return ResponseEntity.badRequest().body(new ErrorResponseDTO(400, "Richiesta non valida"));
   }

    // Gestisce le credenziali di login errate
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(401, "Email o password non validi"));
    }

    // Gestisce errori generici non gestiti esplicitamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(500, "Internal Server Error"));
    }
}