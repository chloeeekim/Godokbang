package chloe.godokbang.repository.custom;

import chloe.godokbang.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationRepositoryCustom {

    Slice<Notification> findNotificationsByUserWithNoOffset(UUID userId, LocalDateTime lastCreatedAt, Long lastId, Pageable pageable);
}
