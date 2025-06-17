package chloe.godokbang.kafka.consumer;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.Notification;
import chloe.godokbang.domain.User;
import chloe.godokbang.dto.request.NotificationRequest;
import chloe.godokbang.dto.response.NotificationResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.repository.NotificationRepository;
import chloe.godokbang.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KafkaNotificationConsumer {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @KafkaListener(topics = "NOTIFICATION", groupId = "noti-group")
    public void consumeNotification(String jsonMessage) {
        try {
            NotificationRequest request = objectMapper.readValue(jsonMessage, NotificationRequest.class);

            Notification notification = request.toEntity(getReceiver(request.getReceiverEmail()), getChatMessage(request.getMessageId()));

            notificationRepository.save(notification);
            convertAndSend(notification.getReceiver().getId(), notification);
        } catch (Exception e) {
            log.error("Failed to consume notification message", e);
        }
    }

    private void convertAndSend(UUID userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/notification." + userId, NotificationResponse.fromEntity(notification));
    }

    private User getReceiver(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    private ChatMessage getChatMessage(Long id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat Message not found."));
    }
}
