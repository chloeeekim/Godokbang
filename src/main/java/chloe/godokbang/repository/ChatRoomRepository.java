package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    public Page<ChatRoom> findByTitleContainingIgnoreCase(Pageable pageable, String keyword);

}
