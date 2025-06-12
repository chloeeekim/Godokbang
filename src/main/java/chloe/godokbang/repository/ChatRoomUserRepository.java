package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    boolean existsByChatRoomIdAndUserId(UUID chatRoomId, UUID userId);
}
