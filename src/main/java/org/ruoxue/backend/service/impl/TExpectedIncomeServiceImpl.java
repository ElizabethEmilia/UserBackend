package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TExpectedIncome;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITExpectedIncomeService;
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
public class TExpectedIncomeServiceImpl extends ServiceImpl<TExpectedIncomeMapper, TExpectedIncome> implements ITExpectedIncomeService {

    @Resource
    private TExpectedIncomeMapper expectedIncomeMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Resource
    private TLogsMapper logsMapper;

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

        Double packBalance = customer.getPackBalance();  //年费余额
        Double taxBalance = customer.getTaxBalance();   //税金余额
        Double otherBalance = customer.getOtherBalance(); //其他余额
        Double income = exchangeMapper.countIncome(uid);    //总收入
        Double outcome = exchangeMapper.countOutcome(uid);    //总收入
        Double lastIncome = exchangeMapper.countLastIncome(uid);    //总收入
        Double lastOutcome = exchangeMapper.countLastOutcome(uid);    //总收入

        JSONObject json = new JSONObject();
        json.put("income", income);
        json.put("lastIncome", lastIncome);
        json.put("outcome", outcome);
        json.put("lastOutcome", lastOutcome);
        json.put("packBalance", packBalance);
        json.put("taxBalance", taxBalance);
        json.put("otherBalance", otherBalance);

        return ResultUtil.success(json);
    }

    @Override
    public Object listExchangeByType(String type, Integer cid, Integer page, Integer size, Date start, Date end, Integer count) {

        if (ToolUtil.isEmpty(type)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("all", null);
        map.put("income", Constant.ExchangeType.INCOME);
        map.put("outcome", Constant.ExchangeType.OUTCOME);

        if(!map.containsKey(type)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(expectedIncomeMapper.countListExchange(map.get(type), cid, start, end, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = expectedIncomeMapper.listExchange(map.get(type), cid, page, size, start, end, XunBinKit.getUid());

        return ResultUtil.success(list);
    }

    @Override
    public Object listExpectIncomeByYear(Integer cid, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(expectedIncomeMapper.countListExpectIncomeByYear(cid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = expectedIncomeMapper.listExpectIncomeByYear(cid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object listExpectIncome(Integer cid, Integer status, Date from, Date to, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(expectedIncomeMapper.countListExpectIncome(cid, status, from, to));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = expectedIncomeMapper.listExpectIncome(cid, status, page, size, from, to);

        return ResultUtil.success(list);
    }

    @Override
    public Object lastExpectTime(Integer cid) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "该公司不存在");
        }

        TExpectedIncome expectedIncome = expectedIncomeMapper.getExpectLast(cid);

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(expectedIncome), -2, expectedIncome, "查询成功", "查询失败");
    }

    @Override
    public Object preSelect(Integer cid, Integer ysaRange) {

        if(ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(ysaRange)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer uid = XunBinKit.getUid();
        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-2, "该用户未登录");
        }

        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(Constant.SallyRange.LESS_THAN_360K, 0.03);
        map.put(Constant.SallyRange.BETWEEN_360K_AND_1M, 0.06);
        //map.put(4, 0.07);

        Double preTaxRatio = map.get(ysaRange);
        if (ToolUtil.isEmpty(preTaxRatio)) {
            return ResultUtil.error(-3, "类型未找见");
        }

        TCustomer customer = (TCustomer) XunBinKit.getSession().getAttribute("obj");
        TCompany company = companyMapper.getCompanyById(cid);
        if (company == null) {
            return ResultUtil.error(-10, "No such company (INTERNAL_ERR)");
        }
        company.setYsaStatus(Constant.YearlySaleAmountStatus.SELECTED);
        company.setPreTaxRatio(preTaxRatio);
        company.updateById();

        TExpectedIncome expectedIncome = new TExpectedIncome();
        expectedIncome.setCid(cid);
        expectedIncome.setStatus(1);
        expectedIncome.setUid(uid);
        expectedIncome.setYsaRange(ysaRange);
        expectedIncome.setPreTaxRatio(preTaxRatio);
        expectedIncome.setTmActivate(new Date());
        expectedIncome.setTmOp(new Date());
        expectedIncome.setTmInactivate(XunBinKit.getYearLastTime());
        expectedIncome.setOper("用户-" + customer.getName());
        boolean b =expectedIncome.insert();

        return XunBinKit.returnResult(b, -4, null, "添加成功", "添加失败");
    }

    @Override
    public Object reselect(Integer cid, Integer ysaRange) {
        // 不再使用
        if(ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(ysaRange)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCompany company = companyMapper.getCompanyById(cid);
        if (company == null) {
            return ResultUtil.error(-10, "No such company (INTERNAL_ERR)");
        }
        company.setYsaStatus(Constant.YearlySaleAmountStatus.MODIFIED);
        //company.setPreTaxRatio(preTaxRatio);
        company.updateById();

//        查出最后一条记录
        TExpectedIncome expectedIncome = expectedIncomeMapper.getExpectLast(cid);
        expectedIncome.setOper(((TCustomer) XunBinKit.getSession().getAttribute("obj")).getName());
        expectedIncome.setStatus(Constant.YearlySaleAmountStatus.MODIFIED);

        if ((expectedIncome.getYsaRange() & ysaRange) == 0) {
            ResultUtil.error(-2, "跨档选择");
        }

        boolean b = expectedIncome.insert();

        return XunBinKit.returnResult(b, -2, null, "添加成功", "添加失败");
    }

    @Override
    public Object complement(Integer cid) {
        if(ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        //        查出最后一条记录
        TExpectedIncome expectedIncome = expectedIncomeMapper.getExpectLast(cid);
        TCustomer customer = customerMapper.getTCustomerByUid(XunBinKit.getUid());
        TCompany company = companyMapper.getCompanyById(cid);

        if (customer == null) {
            return ResultUtil.error(-7, "内部错误：No such uid");
        }

        if (!company.getYsaStatus().equals(Constant.YearlySaleAmountStatus.SHOULD_COMPLEMENT))
            return ResultUtil.error(-11, "此公司无需补交税金。");

        if (customer.getTaxBalance() >= 0) {
            // 如果税金账户余额大于0，说明已经补交，就改状态
            //expectedIncome.setStatus(Constant.YearlySaleAmountStatus.SELECTED);
            //expectedIncome.setId(null);
            //expectedIncome.insert();
            company.setYsaStatus(Constant.YearlySaleAmountStatus.SELECTED);
            company.updateById();
            return ResultUtil.success("系统已确认您的补交。");
        }

        return ResultUtil.error(-10,"您的税金账户余额小于0，" +
                "需要补交税金" + (-customer.getTaxBalance()) + "元，" +
                "请前往“账号信息->在线充值”，在税金账户中充入足够的余额。" +
                "充值完成后，前往本页面再行操作。");
    }

    @Override
    public Object withdraw(Integer cid) {

        if(ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

//        查出最后一条记录
        TExpectedIncome expectedIncome = expectedIncomeMapper.getExpectLast(cid);
//          将状态改为撤回变更(4)
        expectedIncome.setStatus(Constant.PreTaxStallsStatus.WITHDREW);
        expectedIncome.setId(null);
        boolean b = expectedIncome.insert();

        return XunBinKit.returnResult(b, -2, null, "撤回成功", "撤回失败");
    }

    @Override
    public Object listAdminCurrentByYear(Integer cid, String uid, Integer page, Integer size, Integer count) {
        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

//          处理uid
        Integer userid = XunBinKit.getUidByString(uid);

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(expectedIncomeMapper.countListAdminCurrentByYear(cid, userid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = expectedIncomeMapper.listAdminCurrentByYear(cid, userid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object listAdminCurrent(Integer cid, Integer status, Date from, Date to, Integer page, Integer size, String uid, Integer count) {
        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

//          处理uid
        Integer userid = XunBinKit.getUidByString(uid);

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(expectedIncomeMapper.countListAdminCurrent(cid, userid, status, from, to));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = expectedIncomeMapper.listAdminCurrent(cid, userid, status, page, size, from, to);

        return ResultUtil.success(list);
    }

    @Override
    public Object updateStatusByAction(Integer cid, String uid, String action) {

        if (ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(action)) {
            return ResultUtil.error(-1, "参数错误");
        }

//          处理uid
        Integer userid = XunBinKit.getUidByString(uid);

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("request-back", Constant.PreTaxStallsStatus.REQUESTED_WITHDRAW);
        map.put("preselect", Constant.PreTaxStallsStatus.SELECTED);
        map.put("reselect", Constant.PreTaxStallsStatus.RANGE_CHANGEED);

        if (!map.containsKey(action)) {
            return ResultUtil.error(-2, "没有此类型");
        }

        TExpectedIncome expectedIncome = expectedIncomeMapper.getExceptByUidAndCid(userid, cid);

        if (ToolUtil.isEmpty(expectedIncome)) {
            return ResultUtil.error(-3, "没有找到此条记录");
        }

        if (expectedIncome.getStatus() != 0 && expectedIncome.getStatus() != 1) {
            return ResultUtil.error(-4, "您没有权限进行此类转换");
        }


        Integer len = expectedIncomeMapper.updateExpectById(map.get(action), expectedIncome.getId());

        logsMapper.addLog(-1, "修改公司年销售额范围表", 1);

        return XunBinKit.returnResult(len > 0, -5, null, "状态变更成功", "状态变更失败");
    }


}
