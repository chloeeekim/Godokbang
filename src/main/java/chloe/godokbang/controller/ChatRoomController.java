package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.dto.request.CreateChatRoomRequest;
import chloe.godokbang.dto.response.DiscoverListResponse;
import chloe.godokbang.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/discover")
    public String getChatRoomsPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<DiscoverListResponse> rooms;
        if (keyword != null && !keyword.isBlank()) {
            rooms = chatRoomService.searchChatRooms(keyword);
        } else {
            rooms = chatRoomService.getAllChatRoomsList();
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("keyword", keyword);
        model.addAttribute("discoverSelected", true);
        return "pages/chat/discover";
    }

    @GetMapping("/room/new")
    public String getCreateChatRoomPage(Model model) {
        model.addAttribute("createChatRoomRequest", new CreateChatRoomRequest());
        return "pages/chat/createRoom";
    }

    @PostMapping("/room/new")
    public String createChatRoom(@Valid @ModelAttribute CreateChatRoomRequest request, BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createChatRoomRequest", request);
            return "pages/chat/createRoom";
        }

        chatRoomService.createChatRoom(request, userDetails.getUser());
        // TODO 생성 시 바로 채팅방 화면으로
        return "redirect:/chat/rooms";
    }
}
