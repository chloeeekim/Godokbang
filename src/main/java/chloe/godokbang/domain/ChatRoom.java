package chloe.godokbang.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    private int maxUser;

    private int userCount;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUser> users = new ArrayList<>();

    private String latestMsg;

    private LocalDateTime latestMsgAt;

    @Builder
    public ChatRoom(String title, String description, int maxUser, int userCount, String latestMsg, LocalDateTime latestMsgAt) {
        this.title = title;
        this.description = description;
        this.maxUser = maxUser;
        this.userCount = userCount;
        this.latestMsg = latestMsg;
        this.latestMsgAt = latestMsgAt;
    }

    public void incrementUserCount() {
        this.userCount++;
    }

    public void decrementUserCount() {
        this.userCount--;
    }

    public void updateLatest(String latestMsg, LocalDateTime latestMsgAt) {
        this.latestMsg = latestMsg;
        this.latestMsgAt = latestMsgAt;
    }
}
