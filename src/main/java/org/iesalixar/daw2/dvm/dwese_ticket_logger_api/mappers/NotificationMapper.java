package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.notifications.Notification;

import java.time.Instant;

public class NotificationMapper {
    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        return new NotificationDTO(
                notification.getId(),
                notification.getSubject(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static Notification toEntity(NotificationCreateDTO notificationCreateDTO) {
        if (notificationCreateDTO == null) {
            return null;
        }
        return new Notification(
                null,
                notificationCreateDTO.getSubject(),
                notificationCreateDTO.getMessage(),
                notificationCreateDTO.isRead(),
                Instant.now()
        );
    }
}
