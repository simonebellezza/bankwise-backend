package com.example.BankWise.dtos;

    import jakarta.validation.constraints.*;
    import lombok.Data;
    import java.math.BigDecimal;

@Data
    public class TransactionRequestDTO {

        @Positive(message = "L'importo deve essere positivo.")
        private BigDecimal amount;

        @Size(max = 255, message = "La descrizione non può superare i 255 caratteri.")
        private String description;
    }