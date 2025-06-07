package chloe.godokbang.controller;

import chloe.godokbang.dto.request.JoinRequest;
import chloe.godokbang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String getJoinPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest request, BindingResult bindingResult) {
        // email 중복 체크
        if (userService.checkLoginIdExists(request.getEmail())) {
            bindingResult.addError(new FieldError("joinRequest", "email", "이메일이 중복됩니다."));
        }

        // nickname 중복 체크
        if (userService.checkNicknameExists(request.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }

        // password가 passwordCheck와 동일한지 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호 확인이 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }
}
