package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.dto.response.NotificationResponse;
import chloe.godokbang.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public String getNotificationPage(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<NotificationResponse> notifications = notificationService.getNotifications(pageable, userDetails.getUser().getEmail());

        System.out.println("total element: " + notifications.getTotalElements());
        System.out.println("total pages: " + notifications.getTotalPages());
        System.out.println("page size: " + notifications.getPageable().getPageSize());

        model.addAttribute("notifications", notifications);
        model.addAttribute("notiSelected", true);
        return "pages/notification/notification";
    }
}
