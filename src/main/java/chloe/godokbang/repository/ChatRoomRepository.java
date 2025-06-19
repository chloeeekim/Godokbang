package chloe.godokbang.repository;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.User;
import chloe.godokbang.repository.custom.ChatRoomRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID>, ChatRoomRepositoryCustom {

    public Page<ChatRoom> findByTitleContainingIgnoreCase(Pageable pageable, String keyword);

    @Query("""
            SELECT cr FROM ChatRoom cr
            JOIN ChatRoomUser u ON u.chatRoom = cr
            WHERE u.user = :user
            ORDER BY cr.latestMsgAt DESC
            """)
    public List<ChatRoom> findByUserOrderByLatestMsgAtDesc(@Param("user") User user);
}
