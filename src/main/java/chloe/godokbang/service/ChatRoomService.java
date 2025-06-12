package chloe.godokbang.service;

import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.ChatRoomRole;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.ChatRoomUserRepository;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public UUID createChatRoom(CreateChatRoomRequest request, User owner) {
        ChatRoom chatRoom = ChatRoom.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .userCount(1)
                .maxUser(request.getMaxUser())
                .build();
        chatRoomRepository.save(chatRoom);
        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, owner, ChatRoomRole.OWNER));

        return chatRoom.getId();
    }

    public Page<DiscoverListResponse> getAllChatRooms(Pageable pageable) {
        return chatRoomRepository.findAll(pageable)
                .map(DiscoverListResponse::fromEntity);
    }

    public Page<DiscoverListResponse> searchChatRooms(Pageable pageable, String keyword) {
        return chatRoomRepository.findByTitleContainingIgnoreCase(pageable, keyword)
                .map(DiscoverListResponse::fromEntity);
    }

    public void joinRoom() {

    }
}
