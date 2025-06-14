package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatRoomListResponse {

    private UUID id;
    private String title;
    private String latestMsg;
    private String latestMsgAt;
    private int unread;

    @Builder
    public ChatRoomListResponse(UUID id, String title, String latestMsg, LocalDateTime latestMsgAt, int unread) {
        this.id = id;
        this.title = title;
        this.latestMsg = latestMsg;
        this.latestMsgAt = formatDateTime(latestMsgAt);
        this.unread = unread;
    }

    public static ChatRoomListResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomListResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .latestMsg(chatRoom.getLatestMsg())
                .latestMsgAt(chatRoom.getLatestMsgAt())
                .build();
    }

    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
