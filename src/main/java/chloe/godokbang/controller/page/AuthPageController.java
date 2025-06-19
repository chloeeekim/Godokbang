package chloe.godokbang.controller.page;

import chloe.godokbang.auth.CustomUserDetails;
import chloe.godokbang.dto.request.JoinRequest;
import chloe.godokbang.dto.request.LoginRequest;
import chloe.godokbang.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String getHomePage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            // TODO 로그인 되어 있는 경우 home 화면으로 이동
            return "pages/home/home";
        }

        return "pages/sign/welcome";
    }

    @GetMapping("/join")
    public String getJoinPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "pages/sign/join";
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
            return "pages/sign/join";
        }

        userService.join(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "pages/sign/login";
    }

    @PostMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        model.addAttribute("loginRequest", new LoginRequest(email, "")); // 비밀번호는 빈 값
        model.addAttribute("loginError", "Invalid email or password.");
        return "pages/sign/login";
    }
}
