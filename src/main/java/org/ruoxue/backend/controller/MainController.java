package org.ruoxue.backend.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * main控制器
 *
 * @author fengjb
 * date 2018-08-31
 */
@Controller
@RequestMapping("/api")
public class MainController extends BaseController {

    @Resource
    private MainService mainService;

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Object login(@RequestBody JSONObject jsonObject) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return mainService.login(jsonObject);
    }

    @RequestMapping(value = "/verifycode", method = RequestMethod.GET)
    public void gerenateVerifycode(HttpServletRequest request, HttpServletResponse response){
        mainService.gerenateVerifycode(request, response);
    }

}
