package com.banca.bankwise.controllers;

    import com.banca.bankwise.exceptions.AccountNotFoundException;
    import com.banca.bankwise.exceptions.BadRequestException;
    import com.banca.bankwise.exceptions.UserNotFoundException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.http.converter.HttpMessageNotReadableException;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import java.util.List;
    import java.util.stream.Collectors;

@RestControllerAdvice
    public class ExceptionHandlerController {

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<String> handleUsernameNotFoundException(UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        @ExceptionHandler(AccountNotFoundException.class)
        public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<String> handleBadRequestException(BadRequestException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnum(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("Currency")) {
            return ResponseEntity.badRequest().body(
                    "Valore non valido per il campo 'currency'. Valori ammessi: EUR, USD, GBP, INR, JPY.");
        }
        return ResponseEntity.badRequest().body("Richiesta non valida.");
    }


    @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }