package chloe.godokbang.controller;

import chloe.godokbang.auth.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping(value = {"", "/", "/home"})
    public String getHomePage(@RequestParam(value = "logout", required = false) String logout,
            Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "성공적으로 로그아웃되었습니다.");
        }

        if (userDetails != null) {
            model.addAttribute("nickname", userDetails.getUser().getNickname());
        }

        return "pages/home";
    }
}
