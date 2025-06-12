package chloe.godokbang.dto.request;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequest {
    private UUID roomId;
    private String userEmail;
    private String message;
    private MessageType type;

    @Builder
    public ChatMessageRequest(UUID roomId, String userEmail, String message, MessageType type) {
        this.roomId = roomId;
        this.userEmail = userEmail;
        this.message = message;
        this.type = type;
    }

    public ChatMessage toEntity(ChatRoom chatRoom, User sender) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(this.message)
                .type(this.type)
                .build();
    }
}
