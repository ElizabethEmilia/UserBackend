package org.ruoxue.backend.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *  支付功能服务层接口
 */
public interface IAlipayService {

    void startPayment(HttpServletRequest request, HttpServletResponse response, Double amount);

    void finishPaymant(Integer orderid, HttpServletRequest request, HttpServletResponse response)  throws Exception;

    void notifyQuery(Integer orderid, HttpServletResponse response, Map<String, String> paramsMap);

    Object queryOrder(String running);

    Object queryLastOrder();

}
