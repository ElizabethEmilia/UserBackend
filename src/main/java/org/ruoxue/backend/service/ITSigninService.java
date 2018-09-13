package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TSignin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITSigninService extends IService<TSignin> {

    TSignin getSigninByUid(Integer id);

    Object getCustomerByUid(Integer uid);

    Object updateCustomer(TCustomer customer, Integer uid);

    Object deleteCustomer(Integer uid);

    Object listCustomer(Integer page, Integer size);

    Object updatePwssword(String password, Integer uid);

    Object listByType(String type, Integer page, Integer size, String search);
	
}
