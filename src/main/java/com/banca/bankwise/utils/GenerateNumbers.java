package com.banca.bankwise.utils;

import com.banca.bankwise.repositories.AccountRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GenerateNumbers {

    private final AccountRepository accountRepository;
    // Simuliamo per banche solo italiane
    private static final String IBAN_PREFIX = "IT";

    public GenerateNumbers(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    SecureRandom random = new SecureRandom();

    public String generateIban() {
        String iban;

        do {
            StringBuilder stringBuilder = new StringBuilder(IBAN_PREFIX);
            // 27 è la lunghezza dell'Iban. IBAN_PREFIX + 25
            for (int i = 0; i < 25; i++) {
                stringBuilder.append(random.nextInt(10));
            }
            stringBuilder.insert(18, "-");
            stringBuilder.insert(14, "-");
            stringBuilder.insert(10, "-");
            stringBuilder.insert(6, "-");
            iban = stringBuilder.toString();
        } while (accountRepository.existsByIban(iban));

        return iban;
    }

    public String generateAccountNumber() {
        String accountNumber;
        do {

            // Sequenza di 12 numeri generati per conto
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 13; i++) {
                stringBuilder.append(random.nextInt(10));
            }

            accountNumber = stringBuilder.toString();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
