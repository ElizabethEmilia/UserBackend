package org.ruoxue.backend.service.impl;

import com.alipay.api.response.AlipayTradeQueryResponse;
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
        if (ToolUtil.isEmpty(exchange) || exchange.getUid().equals(XunBinKit.getUid())) {
            /// TODO: 添加log  应该不会出现这种情况
            System.err.println("[AlipayService] 该交易不存在或不属于当前用户(id=" +orderid+",uid=" + XunBinKit.getUid() +")");
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
        updateBalanceRelative(amount, exchange.getDst(), exchange);
    }

    private void updateBalanceRelative(Double amount, Integer dst, TExchange exchange) {
        switch (dst) {
            case Constant.ChargeDestination.PACK:
                LoopAction.tryUpToFiveTimes(
                        () -> customerMapper.updatePackBalanceRelative(amount, exchange.getUid()),
                        "更新年费账户余额：" + amount + "元");
                break;
            case Constant.ChargeDestination.TAX:
                LoopAction.tryUpToFiveTimes(
                        () -> customerMapper.updateTaxBalanceRelative(amount, exchange.getUid()),
                        "更新税金账户余额：" + amount + "元");
                break;
            case Constant.ChargeDestination.OTHER:
                LoopAction.tryUpToFiveTimes(
                        () -> customerMapper.updateTaxBalanceRelative(amount, exchange.getUid()),
                        "更新其他账户余额：" + amount + "元");
                break;
            default:
                System.out.println("[AlipayService] Invalid dst=" + dst);
                break;
        }
    }

    @Override
    public Object queryOrderFromAlipay(Integer orderID, AlipayTradeQueryResponse response) {
        TExchange order = exchangeMapper.getEntityByID(orderID);

        // 如果当前不存在，则报错
        if (!ToolUtil.isNotEmpty(order)) {
            return ResultUtil.error(-1,
                    "在支付宝中查询到了订单信息，但是该信息不存在在本地系统中，" +
                            "此问题可能是数据库发生未知错误没有正确处理而导致的。" +
                            "请联系管理员以解决此问题。");
        }

        String tradeStatus = response.getTradeStatus();
        Double amount = Double.parseDouble(response.getTotalAmount());
        int payStatus = (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) ? Constant.PaymentStatus.PAIED :
                (tradeStatus.equals("TRADE_CLOSED") ? Constant.PaymentStatus.CANCELED : Constant.PaymentStatus.UNPAIED);

        // 查到已经支付
        if (payStatus == Constant.PaymentStatus.PAIED) {
            // 之前已支付
            if (order.getState() == Constant.PaymentStatus.PAIED) {
                System.out.println("[Alipay Query] 当前已经支付，且查询订单状况为已支付。");
                return ResultUtil.success(order);
            }

            // 之前未支付或待确认
            if (order.getState() == Constant.PaymentStatus.UNPAIED || order.getState() == Constant.PaymentStatus.NOT_CONFIRMED) {
                System.out.println("[Alipay Query] 当前未支付，或待确认，且查询订单状况为已支付。");
                order.setState(payStatus);
                order.setAmount(amount);
                order.updateById();

                // 增加余额到该用户账户
                updateBalanceRelative(amount, order.getDst(), order);
                return ResultUtil.success(order);
            }
        }
        // 查到仍然未支付或已取消
        if (payStatus == Constant.PaymentStatus.UNPAIED || payStatus == Constant.PaymentStatus.CANCELED) {
            // 之前未支付或已取消
            if (order.getState() == Constant.PaymentStatus.UNPAIED || order.getState() == Constant.PaymentStatus.CANCELED) {
                // 就当无事发生
                return ResultUtil.success(order);
            }
            // 之前已支付（退款）
            if (order.getState() == Constant.PaymentStatus.PAIED) {
                System.out.println("[Alipay Query] 当前已经支付，且查询订单状况为未支付。（发生了退款）");
                order.setState(payStatus);
                order.setAmount(amount);
                order.updateById();

                // 扣除余额到该用户账户
                updateBalanceRelative(-amount, order.getDst(), order);
                return ResultUtil.success(order);
            }
        }

        return ResultUtil.success();
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












