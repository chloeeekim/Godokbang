package chloe.godokbang.repository.custom;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.enums.SortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ChatRoomRepositoryCustom {

    public Slice<ChatRoom> findChatRoomsTitleContainingWithNoOffset(String keyword, LocalDateTime lastAt, UUID lastId, Pageable pageable, SortType sortType);
}
