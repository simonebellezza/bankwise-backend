package com.banca.bankwise.services;

import com.banca.bankwise.dtos.UserRegisterDTO;
import com.banca.bankwise.dtos.UserResponseDTO;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.BadRequestException;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.UserMapper;
import com.banca.bankwise.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if(userRepository.existsByUsername(userRegisterDTO.getUsername())){
            throw new BadRequestException("Username già utilizzato");
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
}
