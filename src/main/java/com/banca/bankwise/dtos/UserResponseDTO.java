package com.banca.bankwise.dtos;

import lombok.*;

import java.util.List;

@Data
public class UserResponseDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
    private List<AccountResponseDTO> accounts;

}
