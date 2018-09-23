package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TTaxAccountDetail;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITTaxAccountDetailService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-14
 */
@Service
public class TTaxAccountDetailServiceImpl extends ServiceImpl<TTaxAccountDetailMapper, TTaxAccountDetail> implements ITTaxAccountDetailService {

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object RechargeMoney(Integer uid, String dst, Double amount) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "用户不存在");
        }

//        向年费余额充值
        if ("pack-balance".equals(dst)) {
            Double packBalance = customer.getPackBalance();
            packBalance += amount;
            customerMapper.updatePackBalance(packBalance, uid);
        }

//        向税金余额充值
        if ("tax-balance".equals(dst)) {
            Double taxBalance = customer.getTaxBalance();
            taxBalance += amount;
            customerMapper.updateTaxBalance(taxBalance, uid);

//            记入异动表
            TTaxAccountDetail taxAccountDetail = new TTaxAccountDetail();
            taxAccountDetail.setTmOp(new Date());
            taxAccountDetail.setChangeAmount(amount);
            taxAccountDetail.setCid(-1);
            taxAccountDetail.setType(Constant.ExchangeType.INCOME);
            taxAccountDetail.insert();
        }

//        向其他余额充值
        if ("other-balance".equals(dst)) {
            Double otherBalance = customer.getOtherBalance();
            otherBalance += amount;
            customerMapper.updateOtherBalance(otherBalance, uid);
        }

        Integer aid = XunBinKit.getUid();

        String aName = "";
        if (ToolUtil.isNotEmpty(aid)) {
            TAdmin admin = adminMapper.getTAdminByUid(aid);
            if (ToolUtil.isNotEmpty(admin)) {
                aName = admin.getName();
            }
        }

        String cName = customer.getName();

//          记入日志
        logsMapper.addLog(aid, aName + "为" + cName + "充值" + amount + "元", 1);

        return ResultUtil.success();
    }

    @Override
    public Object deductionDst(Integer uid, String dst, Double amount, Integer cid, String credit) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(amount) || ToolUtil.isEmpty(dst)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "customer is null");
        }

//        账户金额类型
        String type = "";

//        扣除年费余额
        if ("pack-balance".equals(dst)) {
            Double packBalance = customer.getPackBalance();

            type = "年费余额";
            if (packBalance < amount) {
                return ResultUtil.error(-3, "Sorry, your packBalance is running low");
            }

            packBalance -= amount;
            customerMapper.updatePackBalance(packBalance, uid);
        }

//        扣除税金余额
        if ("tax-balance".equals(dst)) {
            Double taxBalance = customer.getTaxBalance();

            type = "税金余额";
            if (ToolUtil.isEmpty(cid)) {
                return ResultUtil.error(-5, "cid is not null");
            }

            if (taxBalance < amount) {
                return ResultUtil.error(-4, "Sorry, your taxBalance is running low");
            }

            taxBalance -= amount;
            customerMapper.updateTaxBalance(taxBalance, uid);

            TCompany company = companyMapper.getCompanyById(cid);

            if (ToolUtil.isEmpty(company)) {
                return ResultUtil.error(-6, "company is null");
            }

//            记入异动表
            TTaxAccountDetail taxAccountDetail = new TTaxAccountDetail();
            taxAccountDetail.setTmOp(new Date());
            taxAccountDetail.setChangeAmount(amount);
            taxAccountDetail.setCid(cid);
            taxAccountDetail.setCredit(credit);
            taxAccountDetail.setPreTaxRetio(company.getPreTaxRatio());
            taxAccountDetail.setType(Constant.ExchangeType.INCOME);
            taxAccountDetail.insert();
        }

        Integer aid = XunBinKit.getUid();

//        扣除其他余额
        if ("other-balance".equals(dst)) {
            Double otherBalance = customer.getOtherBalance();

            type = "其他余额";
            if (otherBalance < amount) {
                return ResultUtil.error(-7, "Sorry, your otherBalance is running low");
            }

            otherBalance -= amount;
            customerMapper.updateOtherBalance(otherBalance, uid);
        }

        String cName = customer.getName();

//          记入日志
        logsMapper.addLog(aid, "从" + cName + "的账户" + type + "扣除了金额" + amount + "元", 1);

        return ResultUtil.success();
    }

}
