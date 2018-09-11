package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TExchange;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITExchangeService extends IService<TExchange> {

    Object onlinecharge(Integer uid, Integer page, Integer size);

    Object publicchargeStatus(Integer uid, Integer page, Integer size, String status);


}
