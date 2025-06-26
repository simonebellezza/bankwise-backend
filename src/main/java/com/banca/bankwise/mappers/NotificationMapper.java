package com.banca.bankwise.mappers;

import com.banca.bankwise.dtos.NotificationDTO;
import com.banca.bankwise.entities.Notification;

public class NotificationMapper {

    public static NotificationDTO toDto(Notification notification){

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        return notificationDTO;
    }
}
