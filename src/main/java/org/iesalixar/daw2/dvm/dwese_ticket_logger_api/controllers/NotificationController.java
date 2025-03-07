package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ws/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public Flux<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PostMapping
    public Mono<NotificationDTO> createNotifications(@RequestBody NotificationCreateDTO notificationCreateDTO) {
        return notificationService.saveNotification(notificationCreateDTO);
    }
}
