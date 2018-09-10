package org.ruoxue.backend.controller;

import org.apache.http.HttpResponse;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    @GetMapping("/reset")
    public String showRestPasswordPage() {
        if (getSession().getAttribute("uid") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/user.js")
    public @ResponseBody String scriptedUserInfo(HttpResponse response, HttpSession session) {
        if (getSession().getAttribute("uid") == null) {
            // Hans't logged in
            return "console.error('Get this script file without logged in')";
        }

        Integer role = (Integer)session.getAttribute("role");
        String isAdmin = role <= 2 ? "true" : "false";
        String isSuperAdmin = role == 1 ? "true" : "false";

        Object userInfoObj = session.getAttribute("obj");

        String username = "";
        String avatar = "";

        if (userInfoObj instanceof TAdmin) {
            TAdmin admin = (TAdmin)userInfoObj;
            username = admin.getName();
        }
        else if (userInfoObj instanceof TCustomer) {
            TCustomer customer = (TCustomer)userInfoObj;
            username = customer.getName();
            avatar = customer.getAvatar().replaceAll("\"", "\\\"");
        }

        String scripts = "/* This file is auto-generated in order to present basic user info. */\n" +
                "window.config = window.config || {}\n"+
                "window.config.username = \"" + username +"\";\n" +
                "window.config.avatar = \"" + avatar + "\";\n" +
                "window.config.isAdmin = " + isAdmin + "; \n" +
                "window.config.isSuperAdmin = " + isSuperAdmin;
        return scripts;
    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.removeAttribute("uid");
        session.removeAttribute("username");
        session.removeAttribute("obj");
        session.removeAttribute("role");
        return "redirect:/";
    }

}
