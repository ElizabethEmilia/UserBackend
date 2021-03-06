package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.json.CDL;
import org.json.JSONArray;
import org.ruoxue.backend.bean.*;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITReceiptService;
import org.ruoxue.backend.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;
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
public class TReceiptServiceImpl extends ServiceImpl<TReceiptMapper, TReceipt> implements ITReceiptService {

    @Resource
    private TReceiptMapper receiptMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TReceiptStatMapper receiptStatMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object listReceipt(String uid, Integer cid, Integer page, Integer size, Integer type, Integer status, Date start, Date end, Integer count) {
//        非空验证
        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(receiptMapper.countListReceipt(uid, cid, type, status, start, end));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

//        查询list
        List<Map<String, Object>> list = receiptMapper.listReceipt(uid, cid, page, size, type, status, start, end);

        return ResultUtil.success(list);
    }

    @Override
    public Object listTaxStat(Integer cid, Integer page, Integer size, Integer status, Integer type, Date start, Date end) {

//        获取uid
        Integer uid = XunBinKit.getUid();

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

//        查询list
        List<Map<String, Object>> list = receiptMapper.listTaxStat(uid, cid, page, size, type, status, start, end);

        return ResultUtil.success(list);

    }

    @Override
    public Object updateReceiptStat(Integer uid, Integer cid, JSONObject jsonObject) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

//        获取参数
        Double income12 = jsonObject.getDouble("income12");
        Double amountNormal = jsonObject.getDouble("amountNormal");
        Integer timeNormal = jsonObject.getInteger("timeNormal");
        Integer countNormal = jsonObject.getInteger("countNormal");
        Double amountSpec = jsonObject.getDouble("amountSpec");
        Integer timeSpec = jsonObject.getInteger("timeSpec");
        Integer countSpec = jsonObject.getInteger("countSpec");
        Double curPretax = jsonObject.getDouble("curPretax");

        TReceiptStat receiptStat = new TReceiptStat();
        receiptStat.setAid(uid);
        receiptStat.setAmountNormal(amountNormal);
        receiptStat.setAmountSpec(amountSpec);
        receiptStat.setCid(cid);
        receiptStat.setCountNormal(countNormal);
        receiptStat.setCountSpec(countSpec);
        receiptStat.setCurPretax(curPretax);
        receiptStat.setIncome12(income12);
        receiptStat.setTimeNormal(timeNormal);
        receiptStat.setUid(uid);
        receiptStat.setTimeSpec(timeSpec);
        receiptStat.setTmModify(new Date());
        receiptStat.insert();

//        生成hash值
        String hash = Md5Util.getMD5(receiptStat.toString());

        receiptStatMapper.updateStatHash(receiptStat.getId(), hash);

        logsMapper.addLog(-1, "修改统计hash值", 1);

        return ResultUtil.success(receiptStat);
    }

    @Override
    public Object listReceiptStatUpdate(Integer uid, Integer page, Integer size, Integer count) {

//        非空验证
        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(receiptStatMapper.countListReceiptStatPage(uid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = receiptStatMapper.listReceiptStatPage(uid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object receiptRequest(String uid, Integer rid, String action, String reason) {

        if ("submit".equals(action)) {
            return ResultUtil.error(-8, "需要客户自己提交。");
        }

//        非空验证
        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(rid) || ToolUtil.isEmpty(action)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isEmpty(reason)) {
            reason = "";
        }

        Integer userid = XunBinKit.getUidByString(uid);

        TReceipt receipt = receiptMapper.getReceipt(userid, rid);

        if(ToolUtil.isEmpty(receipt)){
            return ResultUtil.error(-2, "开票记录不存在");
        }

//        获取对应的状态
        Integer statusCode = getCodeByAction(action);

        receipt.setStatus(statusCode);
        receipt.setReason(reason);

        boolean b = receipt.updateById();

//        开票审核通过后，加入税金明细表
        if (statusCode == 2) {
            TTaxAccountDetail taxAccountDetail = new TTaxAccountDetail();
            taxAccountDetail.setTmOp(new Date());
            taxAccountDetail.insert();
        }

        // 拒绝提交的时候，加回余额
        if (action.equals("refuse-submit")) {
            TCustomer customer = customerMapper.getTCustomerByUid(receipt.getUid());
            if (ToolUtil.isEmpty(customer)) {
                return ResultUtil.error(-8, "客户不存在（InternalError）");
            }
            customerMapper.updateTaxBalanceRelative(receipt.getPretax(), receipt.getUid());
        }

        // 审核的时候
        if (action.equals("accept")) {
            TCompany company = companyMapper.getCompanyById(receipt.getCid());
            if (company == null) {
                return ResultUtil.error(-10, "No such company");
            }
            // 如果没有设置 tm_first_ec 那么设置
            if (company.getTmFirstEc() == null) {
                company.setTmFirstEc(new Date());
                company.updateById();
            }
        }

        return XunBinKit.returnResult(b, -3, null, "修改成功", "修改失败");
    }

    @Override
    public Object receiptAdd(JSONObject jsonObject) {
//        获取参数
        Integer uid = XunBinKit.getUid();
        Integer cid = jsonObject.getInteger("cid");
        Integer receType = jsonObject.getInteger("recType");
        String cusName = jsonObject.getString("cusName");
        Double recAmount = jsonObject.getDouble("recAmount");
        String address = jsonObject.getString("address");
        String credit = jsonObject.getString("credit");
        String agname = jsonObject.getString("agname");
        String agtaxno = jsonObject.getString("agtaxno");

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(receType) || ToolUtil.isEmpty(cusName) || ToolUtil.isEmpty(recAmount) || ToolUtil.isEmpty(address) || ToolUtil.isEmpty(agname) || ToolUtil.isEmpty(credit) || ToolUtil.isEmpty(agtaxno)) {
            return ResultUtil.error(-1, "参数错误");
        }

        credit = Base64Util.GenerateImageFromDataURI(credit);
        if (ToolUtil.isEmpty(credit)) {
            return ResultUtil.error(-7, "合同附件格式错误");
        }

        TCompany company = companyMapper.getCompanyById(cid);

        if (ToolUtil.isEmpty(company)) {
            return ResultUtil.error(-2, "该公司不存在");
        }

        Double preTaxRatio = company.getPreTaxRatio();

        if (ToolUtil.isEmpty(preTaxRatio)) {
            return ResultUtil.error(-3, "需要设置预交税率");
        }

        Double pretax = recAmount * preTaxRatio;

        TReceipt receipt = new TReceipt();
        receipt.setStatus(0);
        receipt.setCid(cid);
        receipt.setCusName(cusName);
        receipt.setPretax(pretax);
        receipt.setPretaxRatio(preTaxRatio);
        receipt.setReason("");
        receipt.setRecAmount(recAmount);
        receipt.setRecType(receType);
        receipt.setUid(uid);
        receipt.setAddress(address);
        receipt.setTmSubmit(new Date());
        receipt.setTmVallidate(new Date());
        receipt.setAgname(agname);
        receipt.setAgtaxno(agtaxno);
        receipt.setCredit(credit);
        boolean b = receipt.insert();

        return XunBinKit.returnResult(b, -4, null, "开票成功", "开票失败");
    }

    @Override
    public Object receiptList(Integer page, Integer size, Integer cid, Integer type, Integer status, Date start, Date end, Integer count) {

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(receiptMapper.countReceiptList(cid, type, status, start, end, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = receiptMapper.receiptList(cid, type, page, size, status, start, end, XunBinKit.getUid());

        return ResultUtil.success(list);
    }

    @Override
    public Object removeReceipt(Integer rid) {

        if (ToolUtil.isEmpty(rid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer len = receiptMapper.removeReceipt(rid);

        return XunBinKit.returnResult(len > 0, -2, null, "删除成功", "删除失败");
    }

    @Override
    public Object exportReceipt() {

        List<Map<String, Object>> list = receiptMapper.listReceiptAllByUID(XunBinKit.getUid());

        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject json = new JSONObject();
            for (Map.Entry e : map.entrySet()) {
                json.put((String) e.getKey(), e.getValue());
            }
            jsonArray.put(json);
        }

        String time = new SimpleDateFormat("yyyy_MM_dd").format(new Date());

        String csv =CDL.toString(jsonArray);

        String filename = "receipt_" + time;

        HttpServletResponse response = XunBinKit.getResponse();
        response.setHeader("Content-Type", "application/download;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".csv");

        return csv;
    }

    @Override
    public Object statReceipt(Integer page, Integer size, Integer count) {

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(receiptMapper.countStatReceipt(XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = receiptMapper.statReceipt(page, size, XunBinKit.getUid());

        for (Map<String, Object> map : list) {
//            获取公司id
            Integer cid = (Integer) map.get("cid");
//            获取公司名称
            String cName = companyMapper.getCompanyById(cid).getName();
//            获取公司税率
            Double pre_tax_ratio = companyMapper.getCompanyById(cid).getPreTaxRatio();
//            统计12个月收入(含税)
            Double sumAmount = statYearIncome(cid);
//            当月普票次数
            Integer countTimeMonthPTickets = countTimeMonthPTickets(cid);
//            当月普票张数
            Integer countPTickets = countPTickets(cid);
//            当月普票金额
            Double countMonthPTicets = countMonthPTicets(cid);
//            当月专票次数
            Integer countTimeMonthZTickets = countTimeMonthZTickets(cid);
//            当月专票张数
            Integer countZTickets = countZTickets(cid);
//            当月普票金额
            Double countMonthZTicets = countMonthZTicets(cid);
//            当月开票预交税金
            Double countMonthTickets = countMonthTickets(pre_tax_ratio, cid);

            System.out.println(pre_tax_ratio + ", " + sumAmount + ", " + countTimeMonthPTickets + ", " + countPTickets + ", " + countMonthPTicets + ", " + countTimeMonthZTickets + ", " + countZTickets + ", " + countMonthZTicets + ", " + countMonthTickets);

//            将数据塞入map
            map.put("cName", cName);
            map.put("income12", sumAmount);
            map.put("amountNormal", countTimeMonthPTickets);
            map.put("timeNormal", countPTickets);
            map.put("countNormal", countMonthPTicets);
            map.put("amountSpec", countTimeMonthZTickets);
            map.put("timeSpec", countZTickets);
            map.put("countSpec", countMonthZTicets);
            map.put("curPretax", countMonthTickets);
        }

        return ResultUtil.success(list);
    }

    @Override
    public Object updateStatusToSub(Integer rid) {

        if (ToolUtil.isEmpty(rid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TReceipt receipt = receiptMapper.getReceiptById(rid);

        if (ToolUtil.isEmpty(receipt)) {
            return ResultUtil.error(-2, "此开票不存在");
        }

        TCustomer realtimecus = customerMapper.getTCustomerByUid(XunBinKit.getUid());
        if (ToolUtil.isEmpty(realtimecus)) {
            return ResultUtil.error(-10, "INTERNAL_ERR no such customer");
        }

        TCompany company = companyMapper.getCompanyById(receipt.getCid());
        if (ToolUtil.isEmpty(company)) {
            return ResultUtil.error(-10, "INTERNAL_ERR no such company");
        }

        // (1) 档位限制：

        if (company.getYsaRange().equals(Constant.SallyRange.LESS_THAN_360K)) {
            // 当月的已提交的开票税金之和加上当前开票的金额需要小于党委的最大值
            Double PreTaxSummationThisMonth = receiptMapper.countPreTaxSummationMonthly(receipt.getCid());
            Double maxAmountOfSelectedRange = 900.00;

            if (PreTaxSummationThisMonth == null)
                PreTaxSummationThisMonth = 0.00;
            System.out.println(PreTaxSummationThisMonth + " ,, " + receipt.getPretax());

            if (PreTaxSummationThisMonth + receipt.getPretax() > maxAmountOfSelectedRange) {
                return ResultUtil.error(-4, "开票金额大于当月该档位最大的开票金额");
            }
        }
        else if (company.getYsaRange().equals(Constant.SallyRange.BETWEEN_360K_AND_1M)) {
            // 当年的已提交的开票税金之和加上当前开票的金额需要小于党委的最大值
            Double year =  receiptMapper.countPreTaxSummationYearly(receipt.getCid());

            if (year == null)
                year = 0.00;
            Double maxAmountOfTheYear = 1000000.00;

            if (year + receipt.getPretax() > maxAmountOfTheYear) {
                return ResultUtil.error(-5, "开票金额大于当年该档位最大的开票金额");
            }
        }
        else if (company.getYsaRange().equals(Constant.SallyRange.MORE_THAN_1M)) {
            // 当年的已提交的开票税金之和加上当前开票的金额需要小于党委的最大值
            Double year =  receiptMapper.countPreTaxSummationYearly(receipt.getCid());
            Double maxAmountOfTheYear = 5000000.00;

            if (year == null)
                year = 0.00;

            if (year + receipt.getPretax() > maxAmountOfTheYear) {
                return ResultUtil.error(-5, "开票金额大于当年该档位最大的开票金额");
            }
        }
        else {
            return ResultUtil.error(-5, "档位不正确 （" + company.getYsaRange() + ")");
        }

        // (2) 余额限制：剩余税金账户余额需要大于缴纳的税金额
        if (realtimecus.getTaxBalance() < receipt.getPretax()) {
            System.out.println("[余额]" + receipt.getRecAmount());
            return ResultUtil.error(-4, "开票金额大于余额，需要充值" + (receipt.getPretax() - realtimecus.getTaxBalance()) + "元到税金账户。");
        }

        // 扣除等于pretax的税金
        customerMapper.updateTaxBalanceRelative(-receipt.getPretax(), realtimecus.getUid());

        Integer len = receiptMapper.updateStatusToSub(Constant.RECEIPT_STATUS.Submitted, rid);

        Integer uid = XunBinKit.getUid();

        logsMapper.addLog(uid, "修改开票状态", 1);

        TCustomer customer = (TCustomer)XunBinKit.getSession().getAttribute("obj");
        TAdmin admin = adminMapper.getAdminByAid(customer.getAid());

        TPending pending = new TPending();
        pending.setAid(customer.getAid());
        pending.setDescription("客户 " + customer.getName() + " 提交了新的开票申请");
        pending.setUid(customer.getUid());
        pending.setGid(admin.getGid());
        pending.setProcessed(0);
        pending.setReceiver(1);
        pending.setTm(new Date());
        pending.setSenderaid(-1);
        pending.insert();

        return XunBinKit.returnResult(len > 0, -3, null, "Success", "Error");
    }

    @Override
    public Object statReceiptVuser() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "未登录");
        }

        List<Map<String, Object>> list = receiptStatMapper.getStats(uid);

        return ResultUtil.success(list);
    }

    //    当月普票次数
    private Integer countTimeMonthPTickets(Integer cid) {
        List<Integer> list = receiptMapper.countTimePTickets(cid);
        return list.size();
    }

//    当月专票次数
    private Integer countTimeMonthZTickets(Integer cid) {
        List<Integer> list = receiptMapper.countTimeZTickets(cid);
        return list.size();
    }

//    当月开票预交税金
    private Double countMonthTickets(Double pre_tax_ratio, Integer cid) {
        Double countMonthPTicets = countMonthPTicets(cid);
        Double countMonthZTicets = countMonthZTicets(cid);
        if (ToolUtil.isEmpty(countMonthPTicets)) {
            countMonthPTicets = 0.0;
        }
        if (ToolUtil.isEmpty(countMonthZTicets)) {
            countMonthZTicets = 0.0;
        }
        return  (countMonthPTicets + countMonthZTicets ) * pre_tax_ratio;
    }

//    当月普票金额
    private Double countMonthPTicets(Integer cid) {
        return receiptMapper.countMonthPTicets(cid);
    }

//    当月专票金额
    private Double countMonthZTicets(Integer cid) {
        return receiptMapper.countMonthZTicets(cid);
    }

//    当月专票张数
    private Integer countZTickets(Integer cid) {
        return receiptMapper.countZTickets(cid);
    }

//    当月普票张数
    private Integer countPTickets(Integer cid) {
        return receiptMapper.countPTickets(cid);
    }

//    统计12个月收入(含税)
    private Double statYearIncome(Integer cid) {
        TCompany company = companyMapper.getCompanyById(cid);
        Double sum_amount = 0.0;
        if (ToolUtil.isNotEmpty(company)) {
            Date first = company.getTmFirstEc();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(first);
            calendar.add(Calendar.YEAR, 1);
            Date end = calendar.getTime();

            sum_amount = receiptMapper.getYear(cid, first, end);

        }
        return sum_amount;
    }

    private Integer getCodeByAction(String action) {
//        做一个HashMap,存储路径与值得映射
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("submit", Constant.RECEIPT_STATUS.Submitted);
        map.put("refuse-submit", Constant.RECEIPT_STATUS.RefusedWaitingSubmit);
        map.put("accept", Constant.RECEIPT_STATUS.Checked);
        map.put("distrib-dist", Constant.RECEIPT_STATUS.DistributedDistrib);
        map.put("distrib-self", Constant.RECEIPT_STATUS.DistributedSelf);
        map.put("receipt", Constant.RECEIPT_STATUS.Receipted);
        map.put("discard", Constant.RECEIPT_STATUS.Abondoned);
        map.put("pack", Constant.RECEIPT_STATUS.Packed);
        map.put("send", Constant.RECEIPT_STATUS.VerifiedAndSent);
        map.put("selfrecv", Constant.RECEIPT_STATUS.ReceivedCompleted);
        map.put("refuse-packing", Constant.RECEIPT_STATUS.RefusedWaitingPacking);
        map.put("recv", Constant.RECEIPT_STATUS.ReceivedCompleted);
        map.put("reset", Constant.RECEIPT_STATUS.Submitted);
        System.out.println("-------------map: " + map);

        return map.get(action);

    }

}
