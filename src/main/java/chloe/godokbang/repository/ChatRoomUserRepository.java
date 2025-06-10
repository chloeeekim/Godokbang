package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
}
