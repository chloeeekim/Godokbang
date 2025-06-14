package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {

    private UUID id;
    private String title;

    @Builder
    public ChatRoomResponse(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .build();
    }
}
