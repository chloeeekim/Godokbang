package chloe.godokbang.dto.response;

import chloe.godokbang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
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
    private LocalDateTime latestMsgAt;
    private LocalDateTime createdAt;

    @Builder
    public DiscoverListResponse(UUID id, String title, String description, int userCount, int maxUser, boolean joined, LocalDateTime latestMsgAt, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCount = userCount;
        this.maxUser = maxUser;
        this.joined = joined;
        this.latestMsgAt = latestMsgAt;
        this.createdAt = createdAt;
    }

    public static DiscoverListResponse fromEntity(ChatRoom chatRoom, boolean joined) {
        return DiscoverListResponse.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .description(chatRoom.getDescription())
                .userCount(chatRoom.getUserCount())
                .maxUser(chatRoom.getMaxUser())
                .joined(joined)
                .latestMsgAt(chatRoom.getLatestMsgAt())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
