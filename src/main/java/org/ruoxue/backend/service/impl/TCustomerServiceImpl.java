package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.common.constant.ConfigNames;
import org.ruoxue.backend.mapper.MainMapper;
import org.ruoxue.backend.mapper.TConfigMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TCustomerServiceImpl extends ServiceImpl<TCustomerMapper, TCustomer> implements ITCustomerService {

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private MainMapper mainMapper;

    @Resource
    private TSigninMapper signinMapper;

    @Resource
    private TConfigMapper configMapper;

    @Override
    public Object CustomerRegister(JSONObject jsonObject, HttpSession session) {
//        获取参数
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        Integer industry = jsonObject.getInteger("industry");
        Integer type = jsonObject.getInteger("type");
        String msgcode = jsonObject.getString("msgcode");

//        非空检验
        if (ToolUtil.isEmpty(name) || ToolUtil.isEmpty(password) || ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(industry) || ToolUtil.isEmpty(type) || ToolUtil.isEmpty(msgcode)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = mainMapper.getTCustomerByName(name);
        if(ToolUtil.isNotEmpty(customer)){
            return ResultUtil.error(-2, "该用户已注册");
        }

        // 检查短信验证码或图片验证码
        boolean isMsgCodeEnabled = configMapper.getConfigByName(ConfigNames.isSmsEnabled).equals("true");
        // 启动了短信  那么检查短信
        if (isMsgCodeEnabled) {
            String msgcode1 = (String)session.getAttribute("msgcode");
            if (msgcode1 == null || !msgcode1.equals(msgcode)) {
                return ResultUtil.error(-4, "短信验证码错误");
            }
        }
        else {
            // 没有启用短信  检查图片
            String imgcode = (String)session.getAttribute("code");
            if (imgcode == null || !imgcode.equals(msgcode)) {
                return ResultUtil.error(-4, "验证码错误");
            }
        }

        // 禁止同一个手机号码注册多个账号
        if (mainMapper.testIfPhoneNumberExists(phone) != 0) {
            return ResultUtil.error(-5, "该手机号已被注册，请直接登录。若忘记密码，请使用忘记密码功能找回密码。");
        }

//        将客户插入数据库中 --- 获取md5+盐加密后的密码
        String md5SaltPwd = MainServiceImpl.registerUser(password);

        TSignin signin = new TSignin();
//        插入sign表
        signin = insertSignin(md5SaltPwd, msgcode, signin);
        if(ToolUtil.isEmpty(signin)){
            return ResultUtil.error(-3, "添加用户失败");
        }

        customer = new TCustomer();
//        插入customer表
        boolean b = insertCustomer(name, phone, industry, type, customer, signin);

        if(b){
            return ResultUtil.success(0, "添加用户成功");
        } else {
            signin.deleteById();
            return ResultUtil.error(-3, "添加用户失败");
        }

    }

    private TSignin insertSignin(String password, String msgcode, TSignin signin){
        signin.setMsgcode(msgcode);
        signin.setPassword(password);
//        客户的角色为3
        signin.setRole(3);
        signin.setSalt("");
        signin.setToken(XunBinKit.generateToken());
        signin.insert();
        return signin;
    }


//    插入tcustomer表
    private boolean insertCustomer(String name, String phone, Integer industry, Integer type, TCustomer customer, TSignin signin){
        customer.setAddress("");
        customer.setAvatar("");
        customer.setBalance(0.0);
        customer.setCity("");
        customer.setDistrict("");
        customer.setEmail("");
        customer.setFax("");
        customer.setIndustry(industry);
        customer.setLid(signin.getId() + "");
        customer.setName(name);
        customer.setOtherContact("");
        customer.setPaid(0);
        customer.setPhone(phone);
        customer.setQq("");
        customer.setRecType(0);
        customer.setType(type);
        customer.setRegDate(new Date());
        customer.setWechat("");
        return customer.insert();
    }

    @Override
    public Object basicGet(Integer uid) {
//        获取customer实体
        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if(ToolUtil.isEmpty(customer)){
            return ResultUtil.error(-1, "未查到该用户信息");
        }

        return ResultUtil.success(customer);
    }

    @Override
    public Object basicPost(TCustomer customer) {
//        获取用户id
        Integer uid = XunBinKit.getUid();
        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "用户id为空");
        }

        TCustomer cus = customerMapper.getTCustomerByUid(uid);
        if(ToolUtil.isEmpty(cus)){
            ResultUtil.error(-2, "该用户信息为空");
        }

//        将传回来的bean充满
        customer.setUid(uid);
        customer.setLid(cus.getLid());
        customer.setPhone(cus.getPhone());
        customer.setPaid(cus.getPaid());
        customer.setBalance(cus.getBalance());
        customer.setRecType(cus.getRecType());
        customer.setRegDate(cus.getRegDate());
        customer.setStatus(cus.getStatus());
        //boolean b = customer.updateById();
        boolean b = customerMapper.updateCustomer(customer);
        if(b){
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-3, "修改账户信息失败");
        }

    }

    @Override
    public Object password(String old_pwd, String new_pwd) {
//        获取用户id
        Integer uid = XunBinKit.getUid();
//        获取用户bean
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
//        获取signbean
        TSignin signin = signinMapper.getSigninByUid(Integer.parseInt(customer.getLid()));

//        获取加密后的md5原密码
        String oldMd5Pwd = Md5Util.getMD5(old_pwd);
        if(oldMd5Pwd.equals(signin.getPassword())){
            return ResultUtil.error(-1, "原密码错误");
        }

//            将新密码加密
        String newMd5Pwd = Md5Util.getMD5(new_pwd);
        Integer len = signinMapper.updatePassword(newMd5Pwd, signin.getId());
        if(len == 1){
            return ResultUtil.success(0, "密码修改成功");
        } else {
            return ResultUtil.error(-2, "密码修改失败");
        }

    }

    @Override
    public Object avatar(String img) {
//        获取用户id
        Integer uid = XunBinKit.getUid();
//        获取用户bean
        img = Base64Util.GenerateImageFromDataURI(img);
        boolean b = customerMapper.updateAvatar(img, uid);
        if(b){
            return ResultUtil.success(0, "头像修改成功");
        } else {
            return ResultUtil.error(-1, "头像修改失败");
        }
    }

}
