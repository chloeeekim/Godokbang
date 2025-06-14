package chloe.godokbang.dto.request;

import chloe.godokbang.domain.ChatRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateChatRoomRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    private int maxUser;

    @Builder
    public CreateChatRoomRequest(String title, String description, int maxUser) {
        this.title = title;
        this.description = description;
        this.maxUser = maxUser;
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .title(title)
                .description(description)
                .maxUser(maxUser)
                .build();
    }
}
