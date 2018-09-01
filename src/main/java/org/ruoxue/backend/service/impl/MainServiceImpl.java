package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.mapper.MainMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.MainService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class MainServiceImpl extends BaseController implements MainService {

    @Resource
    private MainMapper mainMapper;

    @Resource
    private TSigninMapper signinMapper;

    public Object login(JSONObject jsonObject){
        //        获取参数
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String code = jsonObject.getString("code");

        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(password) || ToolUtil.isEmpty(code)){
            return ResultUtil.error(-1, "请检查您的参数");
        }

//        通过用户名获取管理员
        TAdmin admin = mainMapper.getTAdminByName(name);

//        通过用户名获取客户
        TCustomer customer = mainMapper.getTCustomerByName(name);

//      获取session对象
        HttpSession session = getSession();
//        获取signin对象
        TSignin signin = null;
        if(ToolUtil.isNotEmpty(admin)){
//            管理员
            signin = signinMapper.selectById(admin.getId());
            md5Salt(signin, password, session, code);
        } else if(ToolUtil.isNotEmpty(customer)){
//            客户
            signin = signinMapper.selectById(customer.getUid());
            md5Salt(signin, password, session, code);
        } else {
            return ResultUtil.error(-2, "账号不存在，请注册");
        }

        return ResultUtil.success(1, "登录成功");

    }

    private Object md5Salt(TSignin signin, String password, HttpSession session, String code){

//        md5+盐方式加密
        String passwdMD5 = XunBinKit.md5(password, signin.getSalt());
        if (!signin.getPassword().equals(passwdMD5)) {
            return ResultUtil.error(-3, "密码不正确，请重新输入");
        } else{
//        获取生成的验证码
            String genCode = (String) session.getAttribute("code");
            if(!code.toUpperCase().equals(genCode.toUpperCase())){
                return ResultUtil.error(-4, "验证码不正确");
            }

        }
        session.setAttribute("role", signin.getRole());
        return ResultUtil.success();

    }

    /**
     *  生成验证码
     * @param request
     * @param response
     */
    @Override
    public void gerenateVerifycode(HttpServletRequest request, HttpServletResponse response) {
        //1.画布
        BufferedImage image = new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);
        //2.画笔
        Graphics g = image.getGraphics();
        //3.画背景色
        g.setColor(new Color(255,255,255));
        //4.画验证码的背景
        g.fillRect(0, 0, 200, 100);
        //5.字体
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,55));
        //6.验证码
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<5;i++) {
            String code = generate(1);
            g.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
            int h = (int)(100 * 0.4 + 100 * 0.7 * rand.nextDouble());
            g.drawString(code, 200 / 5 * i, h);
            builder.append(code);
        }
        //7.画一些干扰线
        for(int i=0;i<9;i++) {
            g.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
            g.drawLine(rand.nextInt(200), rand.nextInt(100), rand.nextInt(200), rand.nextInt(100));
        }
        //8.绑定数据，便于登录验证
        HttpSession session = request.getSession();
        session.setAttribute("code", builder.toString());
        //9.压缩，发送给浏览器
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(image, "jpeg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 生成n位验证码
     * 0-9a-zA-Z
     */
    public static String generate(int size) {
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<size;i++) {
            if(rand.nextInt(3)==0) {
                builder.append(rand.nextInt(10));
            } else if(rand.nextInt(3)==1) {
                builder.append((char)(97+rand.nextInt(26)));
            } else {
                builder.append((char)(65+rand.nextInt(26)));
            }
        }
        return builder.toString();
    }


}
