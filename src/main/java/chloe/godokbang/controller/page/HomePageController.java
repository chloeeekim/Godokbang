package chloe.godokbang.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomePageController {

    @GetMapping("")
    public String getHomePage(Model model) {
        model.addAttribute("homeSelected", true);
        return "pages/home/home";
    }
}
