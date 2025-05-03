package com.example.BankWise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDTO {

    @NotBlank(message = "Il nome utente non può essere vuoto.")
    @Size(min = 5, max = 20, message = "Il nome utente deve essere tra 5 e 20 caratteri.")
    private String username;

    @NotBlank(message = "La password non può essere vuota.")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri.")
    private String password;
}
