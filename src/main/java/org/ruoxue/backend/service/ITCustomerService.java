package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TCustomer;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITCustomerService extends IService<TCustomer> {

    Object CustomerRegister(JSONObject jsonObject, HttpSession session);

    Object basicGet(Integer uid);

    Object basicPost(TCustomer customer);

    Object password(String old_pwd, String new_pwd);

    Object avatar(String img);


    /**
     *  9.17
     */
    Object adminAddtime(String uid, Integer cid, Integer months, Double price);

    Object listCharge(Integer uid, Integer page, Integer size, Integer count);

    Object listDeadline();

    Object listDeadlineAdmin();

    Object listExchangeByUid(Integer page, Integer size, Integer count);

    Object dealCustomer(Integer uid, String action);

}
