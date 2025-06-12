package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(UUID roomId);

    @Query("""
        SELECT m FROM ChatMessage m
        WHERE m.chatRoom.id = :roomId
        AND m.type IN ("TEXT", "IMAGE")
        ORDER BY m.sentAt DESC LIMIT 1
    """)
    Optional<ChatMessage> findLatestMessage(@Param("roomId") UUID roomId);
}
