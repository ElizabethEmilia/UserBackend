package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TExchangeMapper;
import org.ruoxue.backend.mapper.TOrderMapper;
import org.ruoxue.backend.mapper.TShopItemsMapper;
import org.ruoxue.backend.service.IAlipayService;
import org.ruoxue.backend.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 *  支付功能服务层实现类
 */
@Service
public class AlipayServiceImpl implements IAlipayService {

    @Resource
    private TShopItemsMapper shopItemsMapper;

    @Resource
    private TOrderMapper orderMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Override
    public void startPayment(HttpServletRequest request, HttpServletResponse response,  Double amount, Integer dst) {

//        获取用户
        Integer uid = XunBinKit.getUid();

        if (uid == null) {
            response.setStatus(403);
            return;
        }

        if (dst < 0 || dst > 2) {
            response.setStatus(400);
            return;
        }

        String[] tyStrs = { "年费", "税金", "其他" };
        String tyString = tyStrs[dst];

        try{

    //        amount(充值)
            if (ToolUtil.isNotEmpty(amount)) {
    //          插入交易表  设置状态为未支付
                Integer orderID;
                TExchange exchange = new TExchange();
                //exchange.setId(Integer.valueOf(orderID.toString()));
                exchange.setUid(uid);
                exchange.setTm(new Date());
                exchange.setPaymethod(Constant.PaymentMethod.ONLINE_ALIPAY);
                exchange.setCid(-1);
                exchange.setState(Constant.ExchangeStatus.UNPAIED);
                exchange.setAmount(amount);
                exchange.setDst(dst);
                exchange.setNote("增薪宝-在线充值" + amount + "元");
                exchange.setType(Constant.ExchangeType.INCOME);

                exchangeMapper.insertReturnsID(exchange);
                orderID = exchangeMapper.getLastID();
                System.err.println("[Alipay New OrderID]" + orderID);

                AlipayUtil.startPayment(orderID.toString(), "增薪宝-" + tyString +" -在线充值" + amount + "元", amount, request, response );
                return;
            }

//          请求参数不正确
            response.setStatus(400) ;
            response.getWriter().write("ERROR! Bad Request");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishPaymant(Integer orderid, HttpServletRequest request, HttpServletResponse response) {
        if (ToolUtil.isEmpty(orderid)) {
            response.setStatus(400);
            return;
        }

        // 确认是否有该订单, 该订单是否属于该用户
        TExchange exchange = exchangeMapper.getEntityByID(orderid);
        if (ToolUtil.isEmpty(exchange) || exchange.getUid() != XunBinKit.getUid()) {
            response.setStatus(404);
            return;
        }

        // 如果状态为未付款更新状态为待确认
        if (exchange.getState() == Constant.PaymentStatus.UNPAIED)
            LoopAction.tryUpToFiveTimes(
                    ()->exchangeMapper.updateStateByID(orderid, Constant.PaymentStatus.NOT_CONFIRMED),
                    "更新状态  订单号: " + exchange.getRunning() + "  ID: " + exchange.getId());
        // 写入支付宝订单号
        // TODO running换成string
        //exchangeMapper.updateRunningByID(orderid, request.getParameter("trade_no"));
    }


    @Override
    public void notifyQuery(Integer orderid, HttpServletResponse response, Map<String, String> paramsMap) {
        if (ToolUtil.isEmpty(orderid)) {
            /// TODO: 添加log  前面有二次校验，应该不会出现这种情况
            System.err.println("[AlipayService] ItemID is null");
            return;
        }
        // 确认是否有该订单, 该订单是否属于该用户
        TExchange exchange = exchangeMapper.getEntityByID(orderid);
        if (ToolUtil.isEmpty(exchange) || exchange.getUid() != XunBinKit.getUid()) {
            /// TODO: 添加log  应该不会出现这种情况
            System.err.println("[AlipayService] exchange is null, no such exchange");
            return;
        }
        // 比较支付宝订单号, 如果不存在， 写入
        if (ToolUtil.isEmpty(exchange.getRunning())) {
            // 写入支付宝订单号
            // TODO: 同上
            //exchangeMapper.updateRunningByID(orderid, paramsMap.get("trade_no"));
        }

        Double amount = Double.parseDouble(paramsMap.get("total_amount"));

        System.err.println("[AlipayService] " + "更新状态  订单号: " + paramsMap.get("trade_no") + "  ID: " + exchange.getId() + "/已支付");

        // 将状态改为已支付
        LoopAction.tryUpToFiveTimes(
                () -> exchangeMapper.updateStateByID(orderid, Constant.PaymentStatus.PAIED),
                "更新状态  订单号: " + paramsMap.get("trade_no") + "  ID: " + exchange.getId());
        // 增加余额到该用户账户
        LoopAction.tryUpToFiveTimes(
                () -> customerMapper.updateBalanceRelative(amount, exchange.getUid()),
                "更新账户余额：" + amount + "元");
    }

    @Override
    public Object queryOrder(String running) {

        if (ToolUtil.isEmpty(running)) {
            return ResultUtil.error(-1, "订单号为空");
        }

        TOrder order = orderMapper.getTOrderByRunning(Long.valueOf(running));

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(order), -2, order, "查询成功", "查询失败");

    }

    @Override
    public Object queryLastOrder() {

        TOrder order = orderMapper.queryLastOrder();

        boolean b = order.getStatus() == Constant.PaymentStatus.NOT_CONFIRMED;

        return XunBinKit.returnResult(b, -2, b, "交易成功", "交易失败");
    }
}












