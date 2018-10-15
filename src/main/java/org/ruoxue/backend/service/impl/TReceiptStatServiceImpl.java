package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.bean.TReceiptStat;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.mapper.TReceiptMapper;
import org.ruoxue.backend.mapper.TReceiptStatMapper;
import org.ruoxue.backend.service.ITReceiptStatService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class TReceiptStatServiceImpl extends ServiceImpl<TReceiptStatMapper, TReceiptStat> implements ITReceiptStatService {

    @Resource
    private TReceiptMapper receiptMapper;

    @Resource
    private TReceiptStatMapper receiptStatMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Override
    public Object receiptStat(String uid, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer userid = XunBinKit.getUidByString(uid);

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(receiptMapper.countStatReceipt(userid));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;


        //List<TReceiptStat> list = receiptStatMapper.listReceiptStat(userid);
        List<Map<String, Object>> list = receiptMapper.statReceipt(page, size, userid);

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
    public Object receiptStatV(String uid) {
        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer userid = XunBinKit.getUidByString(uid);

        List<Map<String, Object>> list = receiptStatMapper.getStats(userid);

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
        System.out.println("-------------map: " + map);

        return map.get(action);

    }

}
