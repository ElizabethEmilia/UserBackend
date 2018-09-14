package org.ruoxue.backend.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.service.ITSigninService;
import org.ruoxue.backend.service.MainService;
import org.ruoxue.backend.service.impl.TConfigServiceImpl;
import org.ruoxue.backend.util.ImageUtil;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

    @ApiOperation("生成验证码接口（滑动）")
    @RequestMapping(value = "/verifycode2", method = RequestMethod.GET)
    @ResponseBody
    public Object gerenateVerifycodeNew(HttpServletRequest request, HttpServletResponse response){
        String path = System.getProperty("user.dir") + "/src/main/resources/res/";
        File codeBackgroundDir = new File(path + "code/");
        JSONObject components = new JSONObject();
        if (!codeBackgroundDir.isDirectory()) {
            return ResultUtil.error(1, "No verification code background avalible");
        }
        try {
            String[] images = codeBackgroundDir.list();
            File imgFile = new File(path + "code/" + images[(int) (Math.random() * images.length)]);
            BufferedImage image = ImageIO.read(imgFile);
            BufferedImage back = ImageIO.read(imgFile);
            BufferedImage mask = ImageIO.read(new File(path +"mask.png"));

            Integer x = ImageUtil.generateCode(image, mask, back, components);
            getSession().setAttribute("vercodepos", x);
            return ResultUtil.success(components);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(2, e.getMessage());
        }
    }

    @ApiOperation("验证验证码")
    @RequestMapping(value = "/verifycode2", method = RequestMethod.POST)
    @ResponseBody
    public Object gerenateVerifycodeNew(Integer x, HttpServletResponse response){
        Integer xs = (Integer)getSession().getAttribute("vercodepos");
        if (xs == null) {
            response.setStatus(403);
            return "Forbidden";
        }
        // 允许正负偏差10px
        if (x < xs-10 || x > xs+10) {
            getSession().removeAttribute("vercodepos");
            return ResultUtil.error(-2, "验证码不正确");
        }
        getSession().setAttribute("verifyok", true);
        return ResultUtil.success();
    }


    @ApiOperation("用户注册接口,同步插入sign表")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody Object CustomerRegister(@RequestBody JSONObject jsonObject, HttpSession session){
        return customerService.CustomerRegister(jsonObject, session);
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
    public @ResponseBody Object forgetPwd(@RequestParam String phone, @RequestParam String msgcode){
        if(ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(msgcode)){
            return ResultUtil.error(-1, "请检查您的参数");
        }

        String storedMsgcode = (String)getSession().getAttribute("msgcode");
        if (storedMsgcode == null || storedMsgcode.equals("")) {
            return ResultUtil.error(-3, "(INTERNAL ERROR) No message sent.");
        }
        if(!msgcode.equals(storedMsgcode)){
            return ResultUtil.error(-3, "短信验证码错误");
        }

        return mainService.forgetPwd(phone);
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
