package chloe.godokbang.domain;

import chloe.godokbang.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    public User(String loginId, String passwordHash, String nickname, UserRole role) {
        this.email = loginId;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.role = role == null ? UserRole.USER : role;
    }
}
