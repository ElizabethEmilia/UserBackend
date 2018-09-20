package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.ITSigninService;
import org.ruoxue.backend.util.Md5Util;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TSigninServiceImpl extends ServiceImpl<TSigninMapper, TSignin> implements ITSigninService {

    @Resource
    private TSigninMapper signinMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object customerAdd(JSONObject jsonObject) {
//        获取参数
        String name = jsonObject.getString("name");
        Integer type = jsonObject.getInteger("type");
        Integer industry = jsonObject.getInteger("industry");
        String phone = jsonObject.getString("phone");
        String email = jsonObject.getString("email");
        String wechat = jsonObject.getString("wechat");
        String qq = jsonObject.getString("qq");
        String fax = jsonObject.getString("fax");
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        String district = jsonObject.getString("district");
        String address = jsonObject.getString("address");
        String password = jsonObject.getString("password");
        Integer aid = jsonObject.getInteger("aid");

        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(password)){
            return ResultUtil.error(-1, "参数错误");
        }

//        检测aid
//        if (XunBinKit.shouldReject(PermissionManager.Moudles.AdminUserAdd)) {
//            XunBinKit.getResponse().setStatus(403);
//            return null;
//        };

        Integer len = adminMapper.countAdminByName(name) + adminMapper.countCustomerByName(name);
        if(len != 0){
            return ResultUtil.error(-2, "该用户已注册");
        }

//        先插入sign表
        TSignin signin = insertSign(password);

        TCustomer customer = new TCustomer();
        customer.setRegDate(new Date());
        customer.setPaid(1);
        customer.setPhone(phone);
        customer.setLid(signin.getId() + "");
        customer.setWechat(wechat);
        customer.setQq(qq);
        customer.setType(type);
        customer.setName(name);
        customer.setIndustry(industry);
        customer.setFax(fax);
        customer.setEmail(email);
        customer.setDistrict(district);
        customer.setCity(city);
        customer.setAddress(address);
        customer.setProvince(province);
        customer.setAid(aid);
        customer.setAvatar("");
        boolean b = customer.insert();

        return XunBinKit.returnResult(b, -3, null, "Success", "Error");
    }

    //    插入sign表
    private TSignin insertSign(String password){
//        将密码加密
        String md5Pwd = Md5Util.getMD5(password);

//        生成一个token
        String token = XunBinKit.generateToken();

        TSignin signin = new TSignin();
        signin.setPassword(md5Pwd);
        signin.setToken(token);
        signin.setSalt("");
        signin.setMsgcode("");
        signin.insert();

        return signin;
    }

    @Override
    public TSignin getSigninByUid(Integer id) {
        return signinMapper.getSigninByUid(id);
    }

    @Override
    public Object getCustomerByUid(Integer uid) {
//        非空验证
        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }

//        获取customer实体
        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if(ToolUtil.isEmpty(customer)){
            return ResultUtil.error(-1, "未查到该用户信息");
        }

        return ResultUtil.success(customer);
    }

    @Override
    public Object updateCustomer(TCustomer customer, Integer uid) {
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
    public Object deleteCustomer(Integer uid) {
//        获取customer实体
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        if(ToolUtil.isEmpty(customer)){
            return ResultUtil.error(-2, "该用户不存在");
        }
        customer.deleteById();
//        获取sign实体
        TSignin signin = signinMapper.getSigninByUid(Integer.parseInt(customer.getLid()));
        boolean b = signin.deleteById();
        if(b){
            return ResultUtil.success(0, "删除用户成功");
        } else {
            return ResultUtil.error(-1, "删除用户失败");
        }
    }

    @Override
    public Object listCustomer(Integer page, Integer size) {
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;
        List<Map<String, Object>> list = customerMapper.listCustomerss(page, size);
        return ResultUtil.success(list);
    }

    @Override
    public Object updatePwssword(String password, Integer uid) {
        if(ToolUtil.isEmpty(password) || ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }
        if(password.length() != 32){
            return ResultUtil.error(-2, "请检查您的密码");
        }

//        获取customer实体
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-3, "该用户不存在");
        }

        TSignin signin = signinMapper.getSigninByUid(Integer.parseInt(customer.getLid()));
        if(ToolUtil.isEmpty(signin)){
            return ResultUtil.error(-4, "该用户不存在");
        }

//        获取加密后的md5密码
        String md5Pwd = Md5Util.getMD5(password);
        Integer len = signinMapper.updatePassword(md5Pwd, signin.getId());
        if(len == 1){
            return ResultUtil.success(0, "修改密码成功");
        } else {
            return ResultUtil.error(-5, "修改密码失败");
        }

    }

    @Override
    public Object listByType(String type, Integer page, Integer size, String search) {

//        session获取uid
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "管理员不存在");
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", type.equals("self") ? signinMapper.listSelf(uid, page, size, search) : null);
        map.put("group", !type.equals("group") ? null : signinMapper.listGroup(uid, page, size, search));
        map.put("all", !type.equals("all") ? null : signinMapper.listAll(page, size, search));

        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(type);

        return ResultUtil.success(list);
    }
}









