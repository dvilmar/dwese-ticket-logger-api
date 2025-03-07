package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.NotificationMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.notifications.Notification;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    public Mono<NotificationDTO> saveNotification (NotificationCreateDTO notificationCreateDTO) {
        Notification notification = NotificationMapper.toEntity(notificationCreateDTO);
        return notificationRepository.save(notification)
                .doOnSuccess(savedNotification ->
                        Mono.fromRunnable(() -> messagingTemplate.convertAndSend(
                                        "/topic/notifications",
                                        NotificationMapper.toDTO (savedNotification)
                                ))
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe()
                )
                .map(NotificationMapper::toDTO);
    }

    public Flux<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().map(NotificationMapper::toDTO);
    }
}
