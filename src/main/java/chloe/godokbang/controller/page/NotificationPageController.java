package chloe.godokbang.controller.page;

import chloe.godokbang.service.NotificationService;
import lombok.RequiredArgsConstructor;
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
    public String getNotificationPage(Model model) {
        model.addAttribute("notiSelected", true);
        return "pages/notification/notification";
    }



    // TODO Notification read 표시 / 삭제 기능 구현

}
