package org.ruoxue.backend.controller;

import org.ruoxue.backend.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 测试thymeleaf
 */
@Controller
@RequestMapping("/thymeleaf")
public class DemoController extends BaseController {

    @RequestMapping("/hello")
    public String hello(Map<String, Object> map) {
        map.put("name", "欢迎使用thymeleaf模板");
        return "hello";
    }


}
