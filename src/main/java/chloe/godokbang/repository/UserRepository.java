package chloe.godokbang.repository;

import chloe.godokbang.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"chatRooms", "chatRooms.chatRoom"})
    Optional<User> findById(UUID id);
}
