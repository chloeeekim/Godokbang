package chloe.godokbang.service;

import chloe.godokbang.config.PaginationProperties;
import chloe.godokbang.domain.Notification;
import chloe.godokbang.dto.response.NotificationResponse;
import chloe.godokbang.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PaginationProperties paginationProperties;

    public Slice<NotificationResponse> getUserNotifications(UUID userId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return notificationRepository.findNotificationsByUserWithNoOffset(userId, lastCreatedAt, lastId, pageRequest)
                .map(NotificationResponse::fromEntity);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    public void markAsUnread(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        notification.markAsUnread();
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
