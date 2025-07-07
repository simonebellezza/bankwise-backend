package com.banca.bankwise.utils;

import com.banca.bankwise.enums.Circuit;
import com.banca.bankwise.repositories.CardRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GenerateCard {

    private final CardRepository cardRepository;

    public GenerateCard(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    String cardNumber;
    SecureRandom random = new SecureRandom();

    public String generateCardNumber(Circuit circuit) {
        do {
            StringBuilder stringBuilder = new StringBuilder(circuit.getPrefix());
            for (int i = 0; i < circuit.getLength() - circuit.getPrefix().length(); i++) {
                stringBuilder.append(random.nextInt(10));
            }
            stringBuilder.insert(12, ' ');
            stringBuilder.insert(8, ' ');
            stringBuilder.insert(4, ' ');
            cardNumber = stringBuilder.toString();
        } while (cardRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    public String generatePin() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= 4; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
