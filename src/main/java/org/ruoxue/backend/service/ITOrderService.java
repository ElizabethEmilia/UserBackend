package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TOrder;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITOrderService extends IService<TOrder> {

    Object ordersStatus(Integer uid, Integer page, Integer size, String status);
	
}
