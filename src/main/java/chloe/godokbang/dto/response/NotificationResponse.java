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

    private Long id;
    private UUID roomId;
    private String roomTitle;
    private String senderNickname;
    private String content;
    private MessageType type;
    private String sentAt;
    private LocalDateTime createdAt;
    private boolean isRead;

    @Builder
    public NotificationResponse(Long id, UUID roomId, String roomTitle, String senderNickname, String content, MessageType type,
                                LocalDateTime sentAt, LocalDateTime createdAt, boolean isRead) {
        this.id = id;
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sentAt = formatDateTime(sentAt);
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static NotificationResponse fromEntity(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .roomId(notification.getMessage().getChatRoom().getId())
                .roomTitle(notification.getMessage().getChatRoom().getTitle())
                .senderNickname(notification.getMessage().getSender().getNickname())
                .content(notification.getMessage().getContent())
                .type(notification.getMessage().getType())
                .sentAt(notification.getMessage().getSentAt())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .build();
    }

    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
