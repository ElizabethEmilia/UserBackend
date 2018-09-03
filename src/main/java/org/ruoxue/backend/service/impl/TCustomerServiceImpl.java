package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.mapper.MainMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.service.MainService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
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
    private MainService mainService;

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
        String md5SaltPwd = MainServiceImpl.registerUser(name, password);

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




}
