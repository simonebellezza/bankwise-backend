package com.banca.bankwise.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username){
        return Jwts.builder()  // Inizia la costruzione del JWT
                .setSubject(username)  // Imposta l'username come subject del token
                .setIssuedAt(new Date())  // Imposta la data di emissione del token (ora attuale)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Imposta la data di scadenza del token
                .signWith(SignatureAlgorithm.HS256, secret)  // Firma il token con l'algoritmo HS256 e la chiave segreta
                .compact();  // Conclude la costruzione del token e lo restituisce come stringa
    }

    public String extractUsername(String token){
        return Jwts.parser()  // Inizia il parsing del token JWT
                .setSigningKey(secret)  // Imposta la chiave segreta
                .parseClaimsJws(token)  // Decodifica e analizza il token JWT
                .getBody()  // Ottiene il corpo (payload) del token
                .getSubject();  // Estrae il subject (username) dal corpo del token
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()  // Inizia il parsing del token JWT
                .setSigningKey(secret)  // Imposta la chiave segreta per la verifica del token
                .parseClaimsJws(token)  // Decodifica e analizza il token JWT
                .getBody()  // Ottiene il corpo (payload) del token
                .getExpiration();  // Estrae la data di scadenza dal corpo del token
        return expirationDate.before(new Date());  // Confronta la data di scadenza con la data corrente per determinare se il token è scaduto
    }
    // Verifica se il token contiene il nome utente corretto e se non è scaduto
    public boolean validateToken(String token, String username){
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}
