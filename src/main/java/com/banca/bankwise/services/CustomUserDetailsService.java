package com.banca.bankwise.services;

import com.banca.bankwise.entities.User;
import com.banca.bankwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca l'utente nel database tramite username
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));

        // Crea e restituisce un oggetto UserDetails con i dati dell'utente
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())           // Imposta lo username
                .password(user.getPassword())               // Imposta la password (codificata)
                .authorities(user.getRole().toString())     // Imposta i ruoli come authorities
                .build();
    }

}
