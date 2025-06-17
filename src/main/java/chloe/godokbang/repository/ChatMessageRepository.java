package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatMessage;
import chloe.godokbang.repository.custom.ChatMessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
}
