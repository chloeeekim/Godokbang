package chloe.godokbang.dto.request;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.Notification;
import chloe.godokbang.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NotificationRequest {

    private String receiverEmail;
    private UUID roomId;
    private Long messageId;

    @Builder
    public NotificationRequest(String receiverEmail, UUID roomId, Long messageId) {
        this.receiverEmail = receiverEmail;
        this.roomId = roomId;
        this.messageId = messageId;
    }

    public Notification toEntity(User receiver, ChatMessage chatMessage) {
        return Notification.builder()
                .receiver(receiver)
                .message(chatMessage)
                .build();
    }
}
