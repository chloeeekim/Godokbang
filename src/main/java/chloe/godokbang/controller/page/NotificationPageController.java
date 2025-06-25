package chloe.godokbang.controller.page;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationPageController {

    private final NotificationService notificationService;

    @GetMapping("")
    public String getNotificationPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("notiSelected", true);
        model.addAttribute("userId", userDetails.getUser().getId());
        return "pages/notification/notification";
    }
}
