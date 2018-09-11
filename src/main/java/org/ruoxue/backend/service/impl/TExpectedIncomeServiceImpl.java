package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TExpectedIncome;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TExchangeMapper;
import org.ruoxue.backend.mapper.TExpectedIncomeMapper;
import org.ruoxue.backend.service.ITExpectedIncomeService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class TExpectedIncomeServiceImpl extends ServiceImpl<TExpectedIncomeMapper, TExpectedIncome> implements ITExpectedIncomeService {

    @Resource
    private TExpectedIncomeMapper expectedIncomeMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Override
    public Object exchangeByRecent() {

//        获取uid
        Integer uid = XunBinKit.getUid();

        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "用户未登录");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if(ToolUtil.isEmpty(customer)){
            return ResultUtil.error(-2, "用户不存在");
        }

        BigDecimal balance = customer.getBalance();         //余额
        BigDecimal income = exchangeMapper.countIncome(uid);    //总收入
        BigDecimal outcome = exchangeMapper.countOutcome(uid);    //总收入
        BigDecimal lastIncome = exchangeMapper.countLastIncome(uid);    //总收入
        BigDecimal lastOutcome = exchangeMapper.countLastOutcome(uid);    //总收入

        JSONObject json = new JSONObject();
        json.put("income", income);
        json.put("lastIncome", lastIncome);
        json.put("outcome", outcome);
        json.put("lastOutcome", lastOutcome);
        json.put("balance", balance);

        return ResultUtil.success(json);
    }

    @Override
    public Object listExchangeByType(String type, Integer cid, Integer page, Integer size, Date start, Date end) {

        if (ToolUtil.isEmpty(type)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("all", null);
        map.put("income", Constant.ExchangeType.INCOME);
        map.put("outcome", Constant.ExchangeType.OUTCOME);

        if(!map.containsKey(type)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TExchange> list = expectedIncomeMapper.listExchange(map.get(type), cid, page, size, start, end);

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "查询失败");
    }


}
