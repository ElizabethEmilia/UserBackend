package org.ruoxue.backend.task;

import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TExpectedIncome;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  定时器,专门用来泡定时任务
 */
@Component
public class MiyukiBinTimer {

    @Resource
    private TCompanyMapper companyMapper;

    @Resource
    private TCustomerMapper customerMapper;

    /**
     *
     *  修改所有客户所有公司的年收入预选状态为“待选择0”
     *  触发时间：每年1月1日0点后
     * 说明: 在年收入预选的操作人设置为“系统”
     */
    @Scheduled(cron = "0 0 0 1 1 ?")              //定时任务,项目跑起来之后自动到时间执行
//    @RequestMapping(value = "", method = RequestMethod.GET)
    public void toSystem() {

//        更新所有用户所有公司 ysa_status = 0
        companyMapper.updateStatus();

//        插入expected表
        List<Map<String, Object>> list = companyMapper.listComAll();
        if (list.size() > 0) {
            for (Map<String, Object> map : list) {
//                  获取公司表中的内容
                Integer uid = (Integer) map.get("uid");
                Integer id = (Integer) map.get("id");
                Double pre_tax_ratio = (Double) map.get("pre_tax_ratio");
                Integer ysa_range = (Integer) map.get("ysa_range");

                TExpectedIncome expectedIncome = new TExpectedIncome();
                expectedIncome.setStatus(Constant.PreTaxStallsStatus.UNSELECTED);
                expectedIncome.setYsaRange(ysa_range);
                expectedIncome.setUid(uid);
                expectedIncome.setCid(id);
                expectedIncome.setPreTaxRatio(pre_tax_ratio);
                expectedIncome.setOper("系统");
                expectedIncome.setTmOp(new Date());
                expectedIncome.insert();
            }
        }
    }

    /**
     *
     *  更改服务包过期用户的状态以及服务包过期时间
     *  触发时间：每年1月1日0点后
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @RequestMapping(value = "", method = RequestMethod.GET)
    public void toSelect() {

        HttpServletResponse response = XunBinKit.getResponse();

//        获取所有的公司
        List<Map<String, Object>> list = companyMapper.listComAll();
        for (Map<String, Object> map : list) {
//            获取到期时间
            Date endDate = (Date) map.get("tax_pack_end");
//            获取当前系统时间
            Date now = new Date();

//            获取公司id
            Integer cid = (Integer) map.get("id");

//            到期用户
            boolean b = endDate.getTime() < now.getTime();
            if (b) {
//                获取uid
                Integer uid = (Integer) map.get("uid");
//                获取customer
                if (ToolUtil.isNotEmpty(uid)) {
                    TCustomer customer = customerMapper.getTCustomerByUid(uid);
                    if (ToolUtil.isNotEmpty(customer)) {
//                        用户余额足够的，直接扣款
                        Double balance = customer.getBalance();
                        if (ToolUtil.isNotEmpty(balance) && balance >= 1200) {
                            balance = balance - 12000;
                            customerMapper.updateBalance(balance, uid);

//                        将到期时间延长一年
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(endDate);
                            calendar.add(Calendar.YEAR, 1);
                            endDate = calendar.getTime();
                            companyMapper.updateEndTime(cid, endDate);

 //        在交易表和订单表分别插入记录 note(增加服务期限X个月)
                            TExchange exchange = new TExchange();
                            exchange.setCid(cid);
                            exchange.setNote("交易");
                            exchange.setAmount(12000.0);
                            exchange.setTm(new Date());
                            exchange.setUid(uid);
                            exchange.insert();

                            TOrder order = new TOrder();
                            order.setAmount(12000.0);
                            order.setCid(cid);
                            order.setTmCreate(new Date());
                            order.insert();

                        }

                    }
                }

            }

        }
    }

}
