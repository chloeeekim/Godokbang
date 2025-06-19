package chloe.godokbang.controller.api;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.domain.enums.SortType;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/discover")
    public Slice<DiscoverListResponse> getChatRoomsForDiscover(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime lastAt,
            @RequestParam(required = false) UUID lastId,
            @RequestParam(defaultValue = "LATEST_MESSAGE") SortType sortType,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return chatRoomService.getChatRoomsForDiscover(keyword, lastAt, lastId, sortType, userDetails.getUser().getId());
    }
}
