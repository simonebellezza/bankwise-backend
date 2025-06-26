package com.banca.bankwise.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "Il nome è obbligatorio.")
    private String firstName;

    @NotBlank(message = "Il cognome è obbligatorio.")
    private String lastName;

    @NotBlank(message = "Il nome utente è obbligatorio.")
    @Size(min = 5, max = 20, message = "Il nome utente deve essere tra 5 e 20 caratteri.")
    private String username;

    @NotBlank(message = "La password è obbligatoria.")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri.")
    private String password;

    @NotBlank(message = "L'email è obbligatoria.")
    @Email(message = "L'email deve essere valida.")
    private String email;

    @NotBlank(message = "Il numero di telefono è obbligatorio.")
    private String phoneNumber;

    @NotBlank(message = "L'indirizzo è obbligatorio.")
    private String address;

    @NotNull(message = "La data di nascita è obbligatoria.")
    @Past(message = "La data di nascita deve essere nel passato.")
    private LocalDate dateOfBirth;

    @AssertTrue(message = "Devi essere maggiorenne (almeno 18 anni).")
    public boolean isAdult() {
        return dateOfBirth != null && Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }
}