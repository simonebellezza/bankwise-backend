package com.example.BankWise.mappers;

import com.example.BankWise.dtos.UserRegisterDTO;
import com.example.BankWise.dtos.UserResponseDTO;
import com.example.BankWise.entities.User;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setFirstName(user.getFirstName());
                userResponseDTO.setLastName(user.getLastName());
                userResponseDTO.setEmail(user.getEmail());
                userResponseDTO.setAddress(user.getAddress());
                userResponseDTO.setPhoneNumber(user.getPhoneNumber());
                userResponseDTO.setDateOfBirth(user.getDateOfBirth().toString());

                // Nella risposta fornisco anche gli account
                if(user.getAccounts()!=null){
                userResponseDTO.setAccounts(user.getAccounts()
                        .stream()
                        .map(AccountMapper::toAccountDTO)
                        .collect(Collectors.toList()));
                }
                return userResponseDTO;
    }

    // Nella registrazione non fornisco gli account e le transazioni
    public static User toUserEntity(UserRegisterDTO userRegisterDTO) {
        User user =  new User();
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setAddress(userRegisterDTO.getAddress());
        user.setUsername(userRegisterDTO.getUsername());
        user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        user.setDateOfBirth(userRegisterDTO.getDateOfBirth());
        return user;
    }
}
