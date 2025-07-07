package com.banca.bankwise.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {

    private String firstName;

    private String lastName;

    @Email(message = "L'email deve essere valida.")
    private String email;

    private String phoneNumber;

    private String address;

    @Past(message = "La data di nascita deve essere nel passato.")
    private LocalDate dateOfBirth;
}
