package chloe.godokbang.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUser> users = new ArrayList<>();

    @Builder
    public ChatRoom(String title, String description, int maxUser, int userCount, User owner) {
        this.title = title;
        this.description = description;
        this.maxUser = maxUser;
        this.userCount = userCount;
        this.owner = owner;
    }

    public void incrementUserCount() {
        this.userCount++;
    }

    public void decrementUserCount() {
        this.userCount--;
    }
}
