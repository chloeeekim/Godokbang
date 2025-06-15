package chloe.godokbang.dto.response;

import chloe.godokbang.domain.Notification;
import chloe.godokbang.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class NotificationResponse {

    private UUID roomId;
    private String roomTitle;
    private String senderNickname;
    private String content;
    private MessageType type;
    private String sentAt;
    private boolean isRead;

    @Builder
    public NotificationResponse(UUID roomId, String roomTitle, String senderNickname, String content, MessageType type,
                                LocalDateTime sentAt, boolean isRead) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sentAt = formatDateTime(sentAt);
        this.isRead = isRead;
    }

    public static NotificationResponse fromEntity(Notification notification, boolean isRead) {
        return NotificationResponse.builder()
                .roomId(notification.getMessage().getChatRoom().getId())
                .roomTitle(notification.getMessage().getChatRoom().getTitle())
                .senderNickname(notification.getMessage().getSender().getNickname())
                .content(notification.getMessage().getContent())
                .type(notification.getMessage().getType())
                .sentAt(notification.getMessage().getSentAt())
                .isRead(isRead)
                .build();
    }

    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
