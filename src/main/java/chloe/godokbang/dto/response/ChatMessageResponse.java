package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private UUID roomId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private String sentAt;

    @Builder
    public ChatMessageResponse(Long id, UUID roomId, String senderNickname, String content, MessageType type, LocalDateTime sentAt) {
        this.id = id;
        this.roomId = roomId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sentAt = formatDateTime(sentAt);
    }

    public static ChatMessageResponse fromEntity(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getChatRoom().getId())
                .senderNickname(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent())
                .type(chatMessage.getType())
                .sentAt(chatMessage.getSentAt())
                .build();
    }

    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
