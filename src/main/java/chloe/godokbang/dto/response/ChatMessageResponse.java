package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {

    private UUID roomId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private LocalDateTime sentAt;

    @Builder
    public ChatMessageResponse(UUID roomId, String senderNickname, String content, MessageType type, LocalDateTime sentAt) {
        this.roomId = roomId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sentAt = sentAt;
    }

    public static ChatMessageResponse fromEntity(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .roomId(chatMessage.getChatRoom().getId())
                .senderNickname(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent())
                .type(chatMessage.getType())
                .sentAt(chatMessage.getSentAt())
                .build();
    }
}
