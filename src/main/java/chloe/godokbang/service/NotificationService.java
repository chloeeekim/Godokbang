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

    /**
     * 유저의 notification 목록 불러오기
     * @param userId 유저 id
     * @param lastCreatedAt 마지막 데이터의 createdAt
     * @param lastId 마지막 데이터의 id
     * @param size 페이지 사이즈
     * @return NotificationResponse dto 목록
     * @see NotificationResponse
     */
    public Slice<NotificationResponse> getUserNotifications(UUID userId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return notificationRepository.findNotificationsByUserWithNoOffset(userId, lastCreatedAt, lastId, pageRequest)
                .map(NotificationResponse::fromEntity);
    }

    /**
     * 알림 읽음 표시
     * @param id 알림 id
     */
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * 알림 안읽음 표시
     * @param id 알림 id
     */
    public void markAsUnread(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        notification.markAsUnread();
        notificationRepository.save(notification);
    }

    /**
     * 알림 삭제
     * @param id 알림 id
     */
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
