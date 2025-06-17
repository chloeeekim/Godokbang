package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.repository.custom.ChatMessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(UUID roomId);
}
