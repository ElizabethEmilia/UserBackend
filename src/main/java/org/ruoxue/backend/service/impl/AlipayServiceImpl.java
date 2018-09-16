package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.bean.TShopItems;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TOrderMapper;
import org.ruoxue.backend.mapper.TShopItemsMapper;
import org.ruoxue.backend.service.IAlipayService;
import org.ruoxue.backend.util.AlipayUtil;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Override
    public void startPayment(HttpServletRequest request, HttpServletResponse response, Integer itemid, String name, Double amount) {

//        订单号为时间戳 + 三位随机数
        Long orderID = XunBinKit.getTimeAppendThreeNum();

//        获取用户的余额
        Double balance = 0.0;
        Integer uid = XunBinKit.getUid();
        TCustomer customer = customerMapper.getTCustomerByUid(uid);
        if (ToolUtil.isNotEmpty(customer)) {
            balance = customer.getBalance();
        }

//        使用商品号查询
        if (ToolUtil.isNotEmpty(itemid) && ToolUtil.isEmpty(name) && ToolUtil.isEmpty(amount)) {

            TShopItems shopItems = shopItemsMapper.getItemById(itemid);

            if (ToolUtil.isEmpty(shopItems)) {
                response.setStatus(503);
                return;
            } else {

//                余额充足
                if (balance >= amount) {

//                在订单表添加一条记录(已付款且不发生充值)
                    TOrder order = new TOrder();
                    order.setRunning(orderID);
                    order.setStatus(Constant.PaymentStatus.PAIED);
                    order.insert();

//                    直接扣款更改余额
                    balance = balance - amount;
                    customerMapper.updateBalance(balance, customer.getUid());

//                在exchange表中插入一条数据
                    TExchange exchange = new TExchange();
                    exchange.setRunning(orderID);
                    exchange.setState(Constant.ExchangeStatus.UNPAIED);
                    exchange.insert();
                    return;
                } else {
//                    余额不足,直接插入订单表
                    TOrder order = new TOrder();
                    order.setRunning(orderID);
                    order.setStatus(Constant.PaymentStatus.UNPAIED);
                    order.insert();
                    AlipayUtil.startPayment(orderID + "", shopItems.getName(), shopItems.getPrice(), request, response );
                    return;
                }
            }
        }

//        name + ammount
        if (ToolUtil.isNotEmpty(name) && ToolUtil.isNotEmpty(amount) && ToolUtil.isEmpty(amount)) {
//            插入order表
            TOrder order = new TOrder();
            order.setStatus(Constant.PaymentStatus.UNPAIED);
            order.setRunning(orderID);
            order.insert();
            AlipayUtil.startPayment(orderID + "", name, amount, request, response );
            return;
        }

//        amount(充值)
        if (ToolUtil.isNotEmpty(amount) && ToolUtil.isEmpty(name) && ToolUtil.isEmpty(itemid)) {
//            只插入交易表
            TExchange exchange = new TExchange();
            exchange.setState(Constant.PaymentStatus.UNPAIED);
            exchange.setAmount(amount);
            exchange.setRunning(orderID);
            exchange.insert();
            return;
        }

//          其他组合直接400 Bad Request
        try {
            response.setStatus(400);
            response.getWriter().write("ERROR! Bad Request");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void finishPaymant(Long orderid, HttpServletRequest request, HttpServletResponse response) {

        if (ToolUtil.isEmpty(orderid)) {
            response.setStatus(400);
            return;
        }

        TOrder order = orderMapper.getTOrderByRunning(orderid);
        if (ToolUtil.isEmpty(order)) {
            response.setStatus(404);
            return;
        }

//        将状态改为待确认
        orderMapper.updateStatus(Constant.PaymentStatus.NOT_CONFIRMED, order.getId());

    }

    @Override
    public void notifyQuery(Long orderid, HttpServletResponse response) {
        if (ToolUtil.isEmpty(orderid)) {
            response.setStatus(400);
            return;
        }

        TOrder order = orderMapper.getTOrderByRunning(orderid);
        if (ToolUtil.isEmpty(order)) {
            response.setStatus(404);
            return;
        }

//        将状态改为已支付
        orderMapper.updateStatus(Constant.PaymentStatus.PAIED, order.getId());
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












