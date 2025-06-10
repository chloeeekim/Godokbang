package chloe.godokbang.domain;

import chloe.godokbang.domain.enums.ChatRoomRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomRole role;

    private LocalDateTime joinedAt;

    @Builder
    public ChatRoomUser(ChatRoom chatRoom, User user, ChatRoomRole role) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }
}
