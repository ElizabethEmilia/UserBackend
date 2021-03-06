package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.*;
import org.ruoxue.backend.common.constant.ConfigNames;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Resource
    private TCompanyMapper companyMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Resource
    private TAdminMapper adminMapper;

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

        if (FormatUtil.isPhoneNumber(name) && !name.equals(phone)) {
            return ResultUtil.error(-7, "不允许使用非本人手机号作为用户名");
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

        logsMapper.addLog(signin.getId(), "添加一个客户", 1);

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
        customer.setPackBalance(0.0);
        customer.setTaxBalance(0.0);
        customer.setOtherBalance(0.0);
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

        if (FormatUtil.isPhoneNumber(customer.getName()) && !customer.getName().equals(cus.getPhone()))
            return ResultUtil.error(-7, "不允许使用非本人手机号作为用户名");

//        将传回来的bean充满
        customer.setUid(uid);
        customer.setLid(cus.getLid());
        customer.setPhone(cus.getPhone());
        customer.setPaid(cus.getPaid());
        customer.setPackBalance(cus.getPackBalance());
        customer.setTaxBalance(cus.getTaxBalance());
        customer.setOtherBalance(cus.getOtherBalance());
        customer.setRecType(cus.getRecType());
        customer.setRegDate(cus.getRegDate());
        customer.setStatus(cus.getStatus());
        //boolean b = customer.updateById();
        boolean b = customerMapper.updateCustomer(customer);
        logsMapper.addLog(uid, "修改客户信息", 1);
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
        logsMapper.addLog(uid, "修改密码", 1);
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
        logsMapper.addLog(uid, "修改头像", 1);
        if(b){
            return ResultUtil.success(0, "头像修改成功");
        } else {
            return ResultUtil.error(-1, "头像修改失败");
        }
    }

    @Override
    public Object adminAddtime(String uid, Integer cid, Integer months, Double price) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(months)) {
            return ResultUtil.error(-1, "参数错误");
        }

//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);

        TCustomer customer = customerMapper.getTCustomerByUid(userid);

        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "该客户不存在");
        }

//        余额不足直接返回403
        Double balance = customer.getPackBalance();
        if (balance < price) {
            XunBinKit.getResponse().setStatus(403);
            return ResultUtil.error(-5, "余额不足");
        }

//        更新余额
        balance -= price;
        customerMapper.updateBalance(balance, userid);
        logsMapper.addLog(-1, "修改余额", 1);

//        获取公司
        TCompany company = companyMapper.getCompanyById(cid);

        if (ToolUtil.isEmpty(company)) {
            return ResultUtil.error(-3, "要充值的公司不存在");
        }

//        获取公司的到期时间
        Date endDate = company.getTaxPackEnd();

//        系统当前时间
        Date now = new Date();

//        给到期时间 续费 +月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.MONTH, months);
        endDate = calendar.getTime();

//        格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        查看续费后是否扔欠费
        boolean c = endDate.getTime() < now.getTime() ? true : false;

//        在交易表和订单表分别插入记录 note(增加服务期限X个月)
        TExchange exchange = new TExchange();
        exchange.setUid(company.getUid());
        exchange.setCid(cid);
        exchange.setNote("增加服务期限" + months + "个月");
        exchange.setAmount(price);
        exchange.setTm(new Date());
        exchange.setUid(userid);
        exchange.setPaymethod(Constant.PaymentMethod.OTHERS);
        exchange.setDst(0);
        exchange.setType(Constant.ExchangeType.OUTCOME);
        exchange.insert();

        TOrder order = new TOrder();
        order.setAmount(price);
        order.setCid(cid);
        order.setTmCreate(new Date());
        order.setTmPaid(new Date());
        order.setStatus(Constant.PaymentStatus.PAIED);
        order.setNote("一站式服务包");
        order.insert();

        companyMapper.updateEndTime(cid, endDate);
        logsMapper.addLog(-1, "修改公司到期时间", 1);
        if (c) {
            customerMapper.updatePaid(userid, 0);
            return ResultUtil.result(-4, sdf.format(endDate), "该公司缴费后仍欠费");
        } else {
            customerMapper.updatePaid(userid, 1);
            return ResultUtil.result(0, sdf.format(endDate), "交费成功");
        }

    }

    @Override
    public Object listCharge(Integer uid, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(exchangeMapper.countGetOnlinecharge(uid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TExchange> list = exchangeMapper.getOnlinecharge(uid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object listDeadline() {

//        获取uid
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }


        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        String user_name = null;
        if(ToolUtil.isNotEmpty(customer)) {
            user_name = customer.getName();
        }

//        先获取客户所有的公司,遍历公司列表,获取到期时间(endDate > now && endDate - 1 < now)的公司直接删除
        List<Map<String, Object>> list = companyMapper.listCompanyAll(uid);
        if (list.size() > 0) {
            Iterator<Map<String,Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                Date endDate = (Date) map.get("tax_pack_end");
                Date now = new Date();
                Calendar calendar =Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.MONTH, -1);
                Date end = calendar.getTime();
                if (endDate.getTime() > now.getTime() && end.getTime() < now.getTime()) {
//                  保留一个月内过期的公司
//                  在返回值中put一个user_name
                    map.put("user_name", user_name);
                } else {
                    it.remove();
                }
            }
        }

        return ResultUtil.success(list);
    }

    @Override
    public Object listDeadlineAdmin() {
        //        获取aid
        Integer aid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(aid)) {
            return ResultUtil.error(-1, "用户未登录");
        }


        Object obj = XunBinKit.getSession().getAttribute("obj");
        if (!(obj instanceof TAdmin)) {
            XunBinKit.getResponse().setStatus(403);
            return null;
        }
        TAdmin admin = (TAdmin)obj;

//        先获取管理员所属客户所有的公司,遍历公司列表,获取到期时间(endDate > now && endDate - 1 < now)的公司直接删除
        List<Map<String, Object>> list = companyMapper.listCompanyAllByAid(aid);
        if (list.size() > 0) {
            Iterator<Map<String,Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                Date endDate = (Date) map.get("tax_pack_end");
                Date now = new Date();
                Calendar calendar =Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.MONTH, -1);
                Date end = calendar.getTime();
                if (endDate.getTime() > now.getTime() && end.getTime() < now.getTime()) {
//                  保留一个月内过期的公司
//                  在返回值中put一个user_name
                    map.put("user_name", map.get("user_name"));
                } else {
                    it.remove();
                }
            }
        }

        return ResultUtil.success(list);
    }

    @Override
    public Object listExchangeByUid(Integer page, Integer size, Integer count) {

//        获取uid
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(exchangeMapper.countGetOnlinecharge(uid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TExchange> list = exchangeMapper.getOnlinecharge(uid, page, size);

        return ResultUtil.success(list);

    }

    @Override
    public Object dealCustomer(Integer uid, String action) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "该客户不存在");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("accept", 1);
        map.put("reject", 2);
        map.put("resubmit", 0);

        if (!map.containsKey(action)) {
            return ResultUtil.error(-3, "请求错误");
        }

        Integer len = customerMapper.updateCustomerCheck(uid, map.get(action));

        return XunBinKit.returnResult(len > 0, -4, null, "Success", "Error");

    }

    @Override
    public Object modifyAID(Integer uid, Integer aid) {
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "该客户不存在");
        }
        TAdmin admin = adminMapper.getAdminByAid(aid);
        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "该管理员不存在");
        }
        customer.setAid(aid);
        customer.updateById();
        return ResultUtil.success();
    }

}
