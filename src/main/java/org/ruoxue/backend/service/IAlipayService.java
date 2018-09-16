package org.ruoxue.backend.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  支付功能服务层接口
 */
public interface IAlipayService {

    void startPayment(HttpServletRequest request, HttpServletResponse response, Integer itemid,
                      String name, Double amount);

    void finishPaymant(Long orderid, HttpServletRequest request, HttpServletResponse response)  throws Exception;

    void notifyQuery(Long orderid, HttpServletResponse response);

    Object queryOrder(String running);

    Object queryLastOrder();

}
