package chloe.godokbang.service;

import chloe.godokbang.config.PaginationProperties;
import chloe.godokbang.domain.ChatRoom;
import chloe.godokbang.domain.ChatRoomUser;
import chloe.godokbang.domain.User;
import chloe.godokbang.domain.enums.ChatRoomRole;
import chloe.godokbang.domain.enums.SortType;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.ChatRoomListResponse;
import chloe.godokbang.dto.response.ChatRoomResponse;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.repository.ChatMessageRepository;
import chloe.godokbang.repository.ChatRoomRepository;
import chloe.godokbang.repository.ChatRoomUserRepository;
import chloe.godokbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PaginationProperties paginationProperties;

    /**
     * 채팅방 생성
     * @param request CreateChatRoomRequest dto
     * @param owner 채팅방을 생성하는 유저
     * @return 생성된 채팅방 id
     */
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

    /**
     * Discover page에서 표시될 채팅방 목록 불러오기
     * @param keyword 검색 키워드
     * @param lastAt 마지막 데이터의 날짜 정보 (createdAt or latestMsgAt)
     * @param lastId 마지막 데이터의 id
     * @param sortType 정렬 기준
     * @param userId 유저 id
     * @return DiscoverListResponse dto 목록
     * @see DiscoverListResponse
     */
    public Slice<DiscoverListResponse> getChatRoomsForDiscover(String keyword, LocalDateTime lastAt, UUID lastId, SortType sortType, UUID userId) {
        PageRequest pageRequest = PageRequest.of(0, paginationProperties.getPageSize());
        Slice<ChatRoom> rooms = chatRoomRepository.findChatRoomsTitleContainingWithNoOffset(keyword, lastAt, lastId, pageRequest, sortType);
        List<UUID> joined = chatRoomUserRepository.findChatRoomIdsByUserId(userId);

        return rooms.map(room -> {
            return DiscoverListResponse.fromEntity(room, joined.contains(room.getId()));
        });
    }

    /**
     * 채팅방 조인
     * @param chatRoomId 채팅방 id
     * @param user 채팅방에 조인하는 유저
     */
    public void joinRoom(UUID chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
        // TODO 기존 join 여부 확인, maxUser 넘는지 확인
        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, user, ChatRoomRole.USER));
        chatRoom.incrementUserCount();
    }

    /**
     * 채팅방 조인 여부 확인
     * @param chatRoomId 채팅방 id
     * @param userId 유저 id
     * @return 채팅방 조인 여부
     */
    public boolean checkAlreadyJoined(UUID chatRoomId, UUID userId) {
        return chatRoomUserRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
    }

    /**
     * 유저가 조인되어 있는 채팅방 목록 불러오기
     * @param userId 유저 id
     * @return ChatRoomListResponse dto 목록
     * @see ChatRoomListResponse
     */
    public List<ChatRoomListResponse> getChatRoomsOfUser(UUID userId) {
        User user = userRepository.findById(userId).get();
        return chatRoomRepository.findByUserOrderByLatestMsgAtDesc(user).stream()
                .map(ChatRoomListResponse::fromEntity)
                .toList();
    }

    /**
     * 채팅방 id로 채팅방 정보 불러오기
     * @param chatRoomId 채팅방 id
     * @return ChatRoomResponse dto
     * @see ChatRoomResponse
     */
    public ChatRoomResponse getChatRoomById(UUID chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroom not found."));
        return ChatRoomResponse.fromEntity(chatRoom);
    }
}
