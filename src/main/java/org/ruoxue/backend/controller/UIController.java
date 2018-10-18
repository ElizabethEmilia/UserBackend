package org.ruoxue.backend.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TConfig;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.mapper.TConfigMapper;
import org.ruoxue.backend.mapper.TOrderMapper;
import org.ruoxue.backend.util.IOUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UIController extends BaseController {

    @Resource
    TConfigMapper configMapper;

    @Resource
    TOrderMapper orderMapper;

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
            //response.setStatus(301);
            //response.setHeader("Location", "/login");
            responseBinary("login2.html", response);
            return;
        }
        responseBinary("index.html", response);
        return ;
    }

    @GetMapping("/login")
    @ResponseBody
    public void showLoginPage(HttpServletResponse response) {
        response.setStatus(301);
        response.setHeader("Location", "/");
        /*if (getSession().getAttribute("uid") != null) {
            response.setStatus(301);
            response.setHeader("Location", "/");
            return;
        }
        responseBinary("login2.html", response);*/
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
        String isAdmin = "false";
        String isSuperAdmin = role != null && role == 1 ? "true" : "false";
        String permission = XunBinKit.getPermission().toString();

        Object userInfoObj = session.getAttribute("obj");

        String username = "";
        String avatar = "";
        String isCustomerPaid = "false";

        String scriptIfAdmin = "";

        if (userInfoObj instanceof TAdmin) {
            TAdmin admin = (TAdmin)userInfoObj;
            username = admin.getName();
            isAdmin = "true";

            scriptIfAdmin = "window.config.adminGroupID = " + admin.getGid() + ";\n";
            scriptIfAdmin += "window.config.adminID = " + admin.getId() + ";\n";
        }
        else if (userInfoObj instanceof TCustomer) {
            TCustomer customer = (TCustomer)userInfoObj;
            username = customer.getName();
            if (customer.getAvatar() != null)
                avatar = customer.getAvatar().replaceAll("\"", "\\\"");
            isCustomerPaid = customer.getPaid() == 1 || customer.getPackBalance() > 0 ? "true" : "false";
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

    // 服务条款
    @GetMapping("/terms")
    @ResponseBody
    public String showTerms() {
        String template = configMapper.getConfigByName("template_terms");

        try {
            template = java.net.URLDecoder.decode(template, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error: " +  e.getMessage();
        }

        return template;
    }

    // 合同
    @GetMapping("/order/agreement/{id}")
    @ResponseBody
    public String showAgreement(@PathVariable("id") Integer id) {
        String template = configMapper.getConfigByName("template_license");

        try {
            template = java.net.URLDecoder.decode(template, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error: " +  e.getMessage();
        }

        Map<String, Object> data = orderMapper.getDetailedOrderInfo(id);
        if (data == null)
            return "No such order ID="+id;

        /// TODO: 取消这里uid判断的注释
        if (!data.get("uid").equals(XunBinKit.getUid())) {
            if (!(XunBinKit.getSession().getAttribute("obj") instanceof TAdmin))
                return "This is not your order";
        }

        String json  = JSONObject.toJSON(data).toString();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>查看合同</title>\n" +
                "    <script src=\"/dist/vue.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"app\">" + template + "</div>\n" +
                "\n" +
                "    <script>\n" +
                "    let vue = new Vue({\n" +
                "        el: '#app',\n" +
                "        data: {\n" +
                "            orderID: '',\n" +
                "            companyName: '',\n" +
                "            companyID: '',\n" +
                "            customerName: '',\n" +
                "            customerID: '',\n" +
                "            amount: '',\n" +
                "            year: '',\n" +
                "            month:'',\n" +
                "            day:'',\n" +
                "            date:'',\n" +
                "            name:'',\n" +
                "            order:" + json + ",\n" +
                "        },\n" +
                "        mounted() {\n" +
                "            this.orderID = this.order.id;\n" +
                "            this.companyName = this.order.companyname;\n" +
                "            this.companyID = this.order.cid;\n" +
                "            this.customerName = this.order.customername;\n" +
                "            this.customerID = this.order.uid;\n" +
                "            this.amount = this.order.amount;\n" +
                "            \n" +
                "            var date = new Date(this.order.tm_create);\n" +
                "            this.year = (date.getFullYear());\n" +
                "            this.month = date.getMonth() + 1;\n" +
                "            this.day = date.getDate() + 1;\n" +
                "            this.date = this.year + '年' + this.month + '月' + this.day + '日';\n" +
                "            this.name =  this.order.note;\n" +
                "        }\n" +
                "    });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

}
