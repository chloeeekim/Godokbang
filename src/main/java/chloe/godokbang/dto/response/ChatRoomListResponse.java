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
    private String description;
    private int userCount;
    private int maxUser;

    @Builder
    public ChatRoomListResponse(UUID id, String title, String description, int userCount, int maxUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCount = userCount;
        this.maxUser = maxUser;
    }

    public static ChatRoomListResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomListResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .description(chatRoom.getDescription())
                .userCount(chatRoom.getUserCount())
                .maxUser(chatRoom.getMaxUser())
                .build();
    }
}
