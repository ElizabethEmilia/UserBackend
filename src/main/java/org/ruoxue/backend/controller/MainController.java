package org.ruoxue.backend.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.service.ITSigninService;
import org.ruoxue.backend.service.MainService;
import org.ruoxue.backend.service.impl.TConfigServiceImpl;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ITCustomerService customerService;

    @Resource
    private ITSigninService signinService;

    @Resource
    private TConfigServiceImpl configService;

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Object login(@RequestBody JSONObject jsonObject) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return mainService.login(jsonObject);
    }

    @ApiOperation("生成验证码接口(大小写字母+数字)")
    @RequestMapping(value = "/verifycode", method = RequestMethod.GET)
    public void gerenateVerifycode(HttpServletRequest request, HttpServletResponse response){
        mainService.gerenateVerifycode(request, response);
    }

    @ApiOperation("用户注册接口,同步插入sign表")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody Object CustomerRegister(@RequestBody JSONObject jsonObject){
        return customerService.CustomerRegister(jsonObject, getSession());
    }

    @ApiOperation("退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(){
        HttpSession session = getSession();
        Integer uid = (Integer) session.getAttribute("uid");
//        往sign表中重新插入一个token
        TSignin signin = signinService.getSigninByUid(uid);
        String token = XunBinKit.generateToken();
        signin.setToken(token);
        signin.updateById();
//        销毁session
        session.invalidate();
    }

    @ApiOperation("找回密码 ")
    @RequestMapping(value = "/forget", method = RequestMethod.POST)
    public @ResponseBody Object forgetPwd(@RequestBody JSONObject jsonObject){
        return mainService.forgetPwd(jsonObject);
    }

    @ApiOperation("重置密码 ")
    @RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
    public @ResponseBody Object resetpwd(@RequestBody JSONObject jsonObject){
        return mainService.resetpwd(jsonObject);
    }

    @RequestMapping(value = "/config.js", method = RequestMethod.GET)
    public void generateConfigJs() throws IOException {
        HttpSession session = getSession();
        Integer role = (Integer) session.getAttribute("role");
//        获取相应对象
        HttpServletResponse response = XunBinKit.getResponse();
//        获取输出流
        PrintWriter pw = response.getWriter();
//        获取配置表中内容
        List<Map<String, Object>> map = configService.getKeyAndValue();

        JSONObject json = new JSONObject();
        for(Map<String, Object> m : map){
                json.put((String) m.get("name"), m.get("value"));
        }
        System.out.println("------------json: " + json);


        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String data = "window.config = " + json + ";\n" +
                "window.config.role = " + role + ";";

        pw.write(data);
        response.setStatus(200);
        pw.close();
    }

}
