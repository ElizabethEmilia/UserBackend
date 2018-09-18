package org.ruoxue.backend.controller;

import org.apache.http.HttpResponse;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.util.IOUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UIController extends BaseController {

    public void responseBinary(String file, HttpServletResponse response) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/";
        file = path + file;
        response.setHeader("X-Resource-Mapped-By", "UIController");
        try {
            byte[] bytes = IOUtil.read(file);
            response.getOutputStream().write(bytes);
        }
        catch (Exception err) {
            response.setStatus(404);
        }
    }

    @GetMapping("/")
    @ResponseBody
    public void showHomePage(HttpServletResponse response) {
        if (getSession().getAttribute("uid") == null) {
            response.setStatus(301);
            response.setHeader("Location", "/login");
            return;
        }
        responseBinary("index.html", response);
        return ;
    }

    @GetMapping("/login")
    @ResponseBody
    public void showLoginPage(HttpServletResponse response) {
        if (getSession().getAttribute("uid") != null) {
            response.setStatus(301);
            response.setHeader("Location", "/");
            return;
        }
        responseBinary("login2.html", response);
    }

    @GetMapping("/reset")
    @ResponseBody
    public void showRestPasswordPage(HttpServletResponse response) {
        if (getSession().getAttribute("uid") != null) {
            response.setStatus(301);
            response.setHeader("Location", "/");
            return;
        }
        responseBinary("login2.html", response);
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
        String permission = XunBinKit.getPermission().toString();

        Object userInfoObj = session.getAttribute("obj");

        String username = "";
        String avatar = "";
        String isCustomerPaid = "false";

        String scriptIfAdmin = "";

        if (userInfoObj instanceof TAdmin) {
            TAdmin admin = (TAdmin)userInfoObj;
            username = admin.getName();

            scriptIfAdmin = "window.config.adminGroupID = " + admin.getGid() + ";\n";
        }
        else if (userInfoObj instanceof TCustomer) {
            TCustomer customer = (TCustomer)userInfoObj;
            username = customer.getName();
            avatar = customer.getAvatar().replaceAll("\"", "\\\"");
            isCustomerPaid = customer.getPaid() == 1 ? "true" : "false";
        }

        String scripts = "/* This file is auto-generated in order to present basic user info. */\n" +
                "window.config = window.config || {}\n"+
                "window.config.username = \"" + username +"\";\n" +
                "window.config.avatar = \"" + avatar + "\";\n" +
                "window.config.isAdmin = " + isAdmin + "; \n" +
                "window.config.isSuperAdmin = " + isSuperAdmin + "; \n" +
                "window.config.isCustomerPaid = " + isCustomerPaid + ";\n" +
                "window.config.permission = " + permission + ";\n";

        scripts += scriptIfAdmin;

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
