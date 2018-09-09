package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.mapper.MainMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Override
    public Object CustomerRegister(JSONObject jsonObject) {
//        获取参数
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        Integer industry = jsonObject.getInteger("industry");
        Integer type = jsonObject.getInteger("type");
        String msgcode = jsonObject.getString("msgcode");

//        非空检验
        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(password) || ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(industry) || ToolUtil.isEmpty(type) || ToolUtil.isEmpty(msgcode)) {
            return ResultUtil.error(-1, "请检查您的参数");
        }

        TCustomer customer = mainMapper.getTCustomerByName(name);
        if(ToolUtil.isNotEmpty(customer)){
            return ResultUtil.error(-2, "该用户已注册");
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
        signin.setToken("");
        signin.insert();
        return signin;
    }


//    插入tcustomer表
    private boolean insertCustomer(String name, String phone, Integer industry, Integer type, TCustomer customer, TSignin signin){
        customer.setAddress("");
        customer.setAvatar("");
        customer.setBalance(BigDecimal.valueOf(0.0));
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
        boolean b = customer.updateById();
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
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        img = Base64Util.GenerateImageFromDataURI(img);
        customer.setAvatar(img);
        boolean b = customer.updateById();
        if(b){
            return ResultUtil.success(0, "头像修改成功");
        } else {
            return ResultUtil.error(-1, "头像修改失败");
        }
    }

}
