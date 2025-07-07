package com.banca.bankwise.services;

import com.banca.bankwise.dtos.UserRegisterDTO;
import com.banca.bankwise.dtos.UserResponseDTO;
import com.banca.bankwise.dtos.UserUpdateDTO;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.UserMapper;
import com.banca.bankwise.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crea un utente
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new BadRequestException("Username già utilizzato");
        }

        // Controlla se l'utente è maggiorenne
        if (Period.between(userRegisterDTO.getDateOfBirth(), LocalDate.now()).getYears() < 18) {
            throw new BadRequestException("Devi essere maggiorenne (almeno 18 anni)");
        }

        User user = UserMapper.toUserEntity(userRegisterDTO);
        // hash password
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        // save
        userRepository.save(user);
        return UserMapper.toUserResponseDTO(user);
    }


    // Ottieni l'utente corrente
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        return UserMapper.toUserResponseDTO(user);
    }

    // Aggiorna l'utente
    public UserResponseDTO updateUser(String username, UserUpdateDTO userDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (userDTO.getAddress() != null) {
            user.setAddress(userDTO.getAddress());
        }

        if (userDTO.getDateOfBirth() != null) {
            // Controlla se l'utente è maggiorenne
            if (Period.between(userDTO.getDateOfBirth(), LocalDate.now()).getYears() < 18) {
                throw new BadRequestException("Devi essere maggiorenne (almeno 18 anni)");
            }
            user.setDateOfBirth(userDTO.getDateOfBirth());
        }

        // Salva l'utente aggiornato
        userRepository.save(user);

        return UserMapper.toUserResponseDTO(user);
    }

    // Elimina l'utente
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        userRepository.delete(user);
    }
}
