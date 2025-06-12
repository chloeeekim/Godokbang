package chloe.godokbang.domain;

import chloe.godokbang.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    private LocalDateTime sentAt;

    @Builder
    public ChatMessage(ChatRoom chatRoom, User sender, String message, MessageType type) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.sentAt = LocalDateTime.now();
    }
}
