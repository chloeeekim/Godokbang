package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    public List<ChatRoom> findByTitleContainingIgnoreCase(String keyword);

}
