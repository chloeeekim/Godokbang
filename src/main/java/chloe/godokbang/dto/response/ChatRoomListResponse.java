package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatRoomListResponse {

    private UUID id;
    private String title;
    private String latestMsg;
    private int unread;

    @Builder
    public ChatRoomListResponse(UUID id, String title, String latestMsg, int unread) {
        this.id = id;
        this.title = title;
        this.latestMsg = latestMsg;
        this.unread = unread;
    }

    public static ChatRoomListResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomListResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .build();
    }
}
