package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DiscoverListResponse {

    private UUID id;
    private String title;
    private String description;
    private int userCount;
    private int maxUser;
    private boolean joined;

    @Builder
    public DiscoverListResponse(UUID id, String title, String description, int userCount, int maxUser, boolean joined) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCount = userCount;
        this.maxUser = maxUser;
        this.joined = joined;
    }

    public static DiscoverListResponse fromEntity(ChatRoom chatRoom, boolean joined) {
        return DiscoverListResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .description(chatRoom.getDescription())
                .userCount(chatRoom.getUserCount())
                .maxUser(chatRoom.getMaxUser())
                .joined(joined)
                .build();
    }
}
