package com.banca.bankwise.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private long id;
    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();
}
