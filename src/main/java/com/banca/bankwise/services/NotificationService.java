package com.banca.bankwise.services;

import com.banca.bankwise.dtos.NotificationDTO;
import com.banca.bankwise.entities.Notification;
import com.banca.bankwise.entities.User;
import com.banca.bankwise.exceptions.UserNotFoundException;
import com.banca.bankwise.mappers.NotificationMapper;
import com.banca.bankwise.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final UserRepository userRepository;

    public NotificationService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<NotificationDTO> getNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
        return user.getNotifications().stream()
                .map((NotificationMapper::toDto))
                .collect(Collectors.toList());
    }
}
