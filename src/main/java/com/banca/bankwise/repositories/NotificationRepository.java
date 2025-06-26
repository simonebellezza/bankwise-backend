package com.banca.bankwise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banca.bankwise.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}

