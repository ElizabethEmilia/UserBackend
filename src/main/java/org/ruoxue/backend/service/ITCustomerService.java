package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.bean.TCustomer;
import com.baomidou.mybatisplus.service.IService;

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
	
}
