package chloe.godokbang.domain;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    private boolean isRead = false;

    @Builder
    public Notification(User receiver, ChatMessage message) {
        this.receiver = receiver;
        this.message = message;
    }

    public void changeIsReadTrue() {
        this.isRead = true;
    }
}
