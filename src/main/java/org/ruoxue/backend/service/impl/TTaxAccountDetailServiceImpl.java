package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.*;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.*;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.service.ITTaxAccountDetailService;
import org.ruoxue.backend.util.Base64Util;
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

    @Resource
    private TPendingMapper pendingMapper;

    @Resource
    private ITCustomerService customerService;

    @Override
    public Object RechargeMoney(Integer uid, String dst, Double amount) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "用户不存在");
        }

        String dstString = "";
        if (dstString.equals("pack-balance")) dstString="年费";
        else if (dstString.equals("tax-balance")) dstString="税金";
        else if (dstString.equals("other-balance")) dstString="其他";

        Integer dstInt = 0;
        if (dstString.equals("pack-balance")) dstInt=0;
        else if (dstString.equals("tax-balance")) dstInt=1;
        else if (dstString.equals("other-balance")) dstInt=2;

        // 加入充值明细表
        TExchange exchange = new TExchange();
        exchange.setUid(uid);
        exchange.setCid(null);
        exchange.setAmount(amount);
        exchange.setPaymethod(Constant.PaymentMethod.OTHERS);
        exchange.setNote("后台充值 - " + dstString + " - " + amount + "元");
        exchange.setDst(dstInt);
        exchange.setTm(new Date());
        exchange.setType(Constant.ExchangeType.INCOME);
        exchange.insert();

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
    public Object deductionDst(Integer uid, String dst, Double amount, Integer cid, String credit, Boolean deduced) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(amount) || ToolUtil.isEmpty(dst)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCustomer customer = customerMapper.getTCustomerByUid(uid);

        if (ToolUtil.isEmpty(customer)) {
            return ResultUtil.error(-2, "customer is null (INTERNAL_ERR)");
        }

//        账户金额类型
        String type = "";

//        扣除年费余额
        if ("pack-balance".equals(dst)) {
            if (ToolUtil.isEmpty(cid)) {
                ResultUtil.error(-1, "参数错误,需要cid");
            }
            Double packBalance = customer.getPackBalance();

            if (packBalance < 0) {
                return ResultUtil.error(-3, "该用户的年费余额账户有欠费。");
            }

            if (deduced) {
                // !!! 在addtime里面已经更新了余额，此处的应该加上再扣
                packBalance += amount;
                customerMapper.updatePackBalance(packBalance, uid);
            }

            type = "年费余额";
            if (packBalance < amount) {
                return ResultUtil.error(-3, "该用户的年费余额账户余额不足。");
            }

            // 续期一年
            customerService.adminAddtime(""+uid, cid, 12, amount);

            // !!! 在addtime里面已经更新了余额，此处的更新余额删掉了。
            //packBalance -= amount;
            //customerMapper.updatePackBalance(packBalance, uid);
        }

//        扣除税金余额
        if ("tax-balance".equals(dst)) {
            Double taxBalance = customer.getTaxBalance();

            type = "税金余额";
            if (ToolUtil.isEmpty(cid)) {
                return ResultUtil.error(-5, "cid is not null (INTERNAL_ERR)");
            }

            TCompany company = companyMapper.getCompanyById(cid);

            boolean shouldComplement = false;

            // deduced = true 由于已经先行扣款  此处不再扣款
            if (!deduced) {
                if (taxBalance < amount) {
                    // 税金余额账户可以为负数余额
                    // 当为负数余额时设置需要补交
                    shouldComplement = true;
                    company.setYsaStatus(Constant.YearlySaleAmountStatus.SHOULD_COMPLEMENT);
                    company.updateById();
                    pendingMapper.sendNotificationToCustomer(company.getUid(), "您的公司" + company.getName() +"需要补交税金，请注意。", new Date());
                    logsMapper.addLog(XunBinKit.getUid(), "从" + customer.getName() + "的税金账户不足以支付税金，需要客户补交。", 1);
                }

                taxBalance -= amount;
                customerMapper.updateTaxBalance(taxBalance, uid);
            }

            if (ToolUtil.isEmpty(company)) {
                return ResultUtil.error(-6, "company is null (INTERNAL_ERR)");
            }

            // credit是图片
            if (credit == null)
                credit = "";

            if (!credit.equals("")) {
                credit = Base64Util.GenerateImageFromDataURI(credit);
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
                return ResultUtil.error(-7, "该客户的其他余额账户余额不足");
            }

            TExchange exchange = new TExchange();
            exchange.setUid(customer.getUid());
            exchange.setCid(null);
            exchange.setNote("从其他金额中扣除");
            exchange.setAmount(amount);
            exchange.setTm(new Date());
            exchange.setUid(customer.getUid());
            exchange.setPaymethod(Constant.PaymentMethod.OTHERS);
            exchange.setDst(2);
            exchange.setType(Constant.ExchangeType.OUTCOME);
            exchange.insert();

            if (deduced) {
                otherBalance -= amount;
                customerMapper.updateOtherBalance(otherBalance, uid);
            }
        }

        String cName = customer.getName();

//          记入日志
        logsMapper.addLog(aid, "从" + cName + "的账户" + type + "扣除了金额" + amount + "元", 1);

        return ResultUtil.success();
    }

}
