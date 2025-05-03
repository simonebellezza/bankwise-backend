package com.banca.bankwise.filters;

import com.banca.bankwise.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    // Il metodo doFilterInternal estende OncePerRequestFilter e viene chiamato per ogni richiesta HTTP
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //  recupera il valore dell'intestazione Authorization della richiesta HTTP (Bearer <tokrn>)
        String authorizationHeader = request.getHeader("Authorization");

        // Se Authorization è presente e inizia con "Bearer " estrae il token e il nome utente
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            // Se il nome utente non è nullo e l'autenticazione non è già presente nel contesto di sicurezza
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carica i dettagli dell'utente dal servizio UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Se il token è valido, crea un oggetto di autenticazione e lo imposta nel contesto di sicurezza
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    // Crea un oggetto di autenticazione con i dettagli dell'utente e le sue autorità
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        // Continua la catena di filtri
        filterChain.doFilter(request, response);
    }
}