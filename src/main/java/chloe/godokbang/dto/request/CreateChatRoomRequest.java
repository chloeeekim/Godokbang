package chloe.godokbang.dto.request;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateChatRoomRequest {

    @NotBlank(message = "채팅방 이름이 입력되지 않았습니다.")
    private String title;

    private String description;

    private int maxUser;

    @Builder
    public CreateChatRoomRequest(String title, String description, int maxUser) {
        this.title = title;
        this.description = description;
        this.maxUser = maxUser;
    }

    public ChatRoom toEntity(User user) {
        return ChatRoom.builder()
                .title(title)
                .description(description)
                .maxUser(maxUser)
                .owner(user)
                .build();
    }
}
