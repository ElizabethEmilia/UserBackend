package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.*;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITPublicChargeService;
import org.ruoxue.backend.util.Base64Util;
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
public class TPublicChargeServiceImpl extends ServiceImpl<TPublicChargeMapper, TPublicCharge> implements ITPublicChargeService {

    @Resource
    private TPublicChargeMapper publicChargeMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object updatePublicchargeStatus(Integer uid, Integer pid, String status) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(pid) || ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("confirm", Constant.PublicChargeState.CONFIRMED);
        map.put("cancel", Constant.PublicChargeState.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        Integer len = publicChargeMapper.updatePublicChangeStatus(pid, map.get(status));

        if(status.equals("confirm")) {
            Double amount = publicChargeMapper.getPublicChargeAmount(pid);

            TExchange exchange = new TExchange();
            //exchange.setUid(company.getUid());
            exchange.setCid(null);
            exchange.setNote("对公充值 - " + amount + "元");
            exchange.setAmount(amount);
            exchange.setTm(new Date());
            //exchange.setUid(userid);
            exchange.setPaymethod(Constant.PaymentMethod.OTHERS);
            exchange.setDst(0);
            exchange.setType(Constant.ExchangeType.INCOME);
            exchange.insert();
            // 增加余额
            customerMapper.updatePackBalanceRelative(publicChargeMapper.getPublicChargeAmount(pid), uid);
        }

        logsMapper.addLog(-1, "修改对公充值表", 1);

        return XunBinKit.returnResult(len > 0, -2, null,"修改成功", "修改失败");
    }

    @Override
    public Object publChargeAdd(JSONObject jsonObject) {
//        获取参数
        Integer type = jsonObject.getInteger("type");
        Double amount = jsonObject.getDouble("amount");
        String name = jsonObject.getString("name");
        String account = jsonObject.getString("account");
        String bank = jsonObject.getString("bank");
        String credit = jsonObject.getString("credit");

//        转换图片
        credit = Base64Util.GenerateImageFromDataURI(credit);

//        获取uid
        Integer uid = XunBinKit.getUid();

        if(ToolUtil.isEmpty(type) || ToolUtil.isEmpty(amount) || ToolUtil.isEmpty(name) || ToolUtil.isEmpty(account) || ToolUtil.isEmpty(bank) || ToolUtil.isEmpty(credit) || ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }

        TPublicCharge publicCharge = new TPublicCharge();
        publicCharge.setAccount(account);
        publicCharge.setAmount(amount);
        publicCharge.setBank(bank);
        publicCharge.setCredit(credit);
        publicCharge.setName(name);
        publicCharge.setType(type);
        publicCharge.setUid(uid);
        publicCharge.setStatus(0);
        publicCharge.setTmCreate(new Date());
        boolean b = publicCharge.insert();

        TCustomer customer = (TCustomer)XunBinKit.getSession().getAttribute("obj");
        TAdmin admin = adminMapper.getAdminByAid(customer.getAid());

        TPending pending = new TPending();
        pending.setAid(customer.getAid());
        pending.setDescription("客户 " + customer.getName() + " 提交了新的对公充值申请");
        pending.setUid(customer.getUid());
        pending.setGid(admin.getGid());
        pending.setProcessed(0);
        pending.setReceiver(1);
        pending.setTm(new Date());
        pending.setSenderaid(-1);
        pending.insert();

        return XunBinKit.returnResult(b, -2, null, "添加成功", "添加失败");
    }

    @Override
    public Object publChargeCancel(Integer id) {
        if(ToolUtil.isEmpty(id)){
            return ResultUtil.error(-1, "参数错误");
        }

        Integer len = publicChargeMapper.updatePublicChangeStatus(id, 2);

        logsMapper.addLog(-1, "修改对公充值表", 1);

        return XunBinKit.returnResult(len > 0, -2, null, "取消成功", "取消失败");
    }

    @Override
    public Object listPublChargeStatus(Integer page, Integer size, String status, Date start, Date end, Integer count) {

//        获取uid
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(status) || ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("all", null);
        map.put("pending", Constant.PublicChargeState.WAITING);
        map.put("confirmed", Constant.PublicChargeState.CONFIRMED);
        map.put("cancelled", Constant.PublicChargeState.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(publicChargeMapper.countListPublicCharge(uid, map.get(status), start, end));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TPublicCharge> list = publicChargeMapper.listPublicCharge(uid, page, size, map.get(status), start, end);

        return ResultUtil.success(list);
    }
}
