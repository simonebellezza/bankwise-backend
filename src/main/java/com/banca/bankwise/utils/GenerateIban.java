package com.banca.bankwise.utils;

import com.banca.bankwise.repositories.AccountRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GenerateIban {

    private final AccountRepository accountRepository;
    // Simuliamo per banche solo italiane
    private static final String IBAN_PREFIX = "IT";

    public GenerateIban(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public  String generateIban(){
        String iban;
        // Algoritmi più robusti utilizzati per la crittografia
        SecureRandom random = new SecureRandom();

        do{
            StringBuilder stringBuilder = new StringBuilder(IBAN_PREFIX);
            // 27 è la lunghezza dell'IBAN in Italia. IBAN_PREFIX + 25
            for(int i = 0; i <= 25; i++){
                stringBuilder.append(random.nextInt(10));
            }
            iban = stringBuilder.toString();
        } while (accountRepository.existsByIban(iban));

        return iban;
    }
}
