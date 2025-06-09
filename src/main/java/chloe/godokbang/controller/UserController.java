package chloe.godokbang.controller;

import chloe.godokbang.dto.request.JoinRequest;
import chloe.godokbang.dto.request.LoginRequest;
import chloe.godokbang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String getJoinPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "pages/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest request, BindingResult bindingResult) {
        // email 중복 체크
        if (userService.checkLoginIdExists(request.getEmail())) {
            bindingResult.addError(new FieldError("joinRequest", "email", "Your email is already taken."));
        }

        // nickname 중복 체크
        if (userService.checkNicknameExists(request.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "Your nickname is already taken."));
        }

        // password가 passwordCheck와 동일한지 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "Passwords do not match."));
        }

        if (bindingResult.hasErrors()) {
            return "pages/join";
        }

        userService.join(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
}
