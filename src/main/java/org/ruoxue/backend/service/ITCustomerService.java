package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TCustomer;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITCustomerService extends IService<TCustomer> {

    Object CustomerRegister(JSONObject jsonObject);

    Object basicGet(Integer uid);

    Object basicPost(TCustomer customer);

    Object password(String old_pwd, String new_pwd);

    Object avatar(String img);
	
}
