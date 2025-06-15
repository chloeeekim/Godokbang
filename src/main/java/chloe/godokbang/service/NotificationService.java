package chloe.godokbang.service;

import chloe.godokbang.domain.Notification;
import chloe.godokbang.domain.User;
import chloe.godokbang.dto.response.NotificationResponse;
import chloe.godokbang.repository.NotificationRepository;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Page<NotificationResponse> getNotifications(Pageable pageable, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Page<Notification> notis = notificationRepository.findByReceiver(pageable, user);

        return notis.map(noti -> {
            return NotificationResponse.fromEntity(noti, noti.isRead());
        });
    }
}
