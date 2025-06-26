package com.banca.bankwise.controllers;

import com.banca.bankwise.dtos.NotificationDTO;
import com.banca.bankwise.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController (NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Recupera le notifiche dell'utente autenticato")
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> findAllNotifications(Principal principal) {
        List<NotificationDTO> notifications = notificationService.getNotifications(principal.getName());
        return ResponseEntity.ok(notifications);
    }
}
