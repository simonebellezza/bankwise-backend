package com.example.BankWise.dtos;

import com.example.BankWise.entities.Account;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UserResponseDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
    private List<AccountDTO> accounts;

}
