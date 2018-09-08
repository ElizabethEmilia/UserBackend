package org.ruoxue.backend.controller;

import org.ruoxue.backend.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController extends BaseController {

    @GetMapping("/")
    public String showHomePage() {
        if (getSession().getAttribute("uid") == null) {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        if (getSession().getAttribute("uid") != null) {
            return "redirect:/";
        }
        return "login";
    }

}
