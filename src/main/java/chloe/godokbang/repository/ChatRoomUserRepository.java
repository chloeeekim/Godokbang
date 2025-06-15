package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    boolean existsByChatRoomIdAndUserId(UUID chatRoomId, UUID userId);

    @Query("""
            SELECT cru.chatRoom.id
            FROM ChatRoomUser cru
            WHERE cru.user.id = :userId
            """)
    List<UUID> findChatRoomIdsByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT cru.user
            FROM ChatRoomUser cru
            WHERE cru.chatRoom.id = :roomId
            """)
    List<User> findUsersByChatRoomId(@Param("roomId") UUID roomId);
}
