package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TOrder;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITOrderService extends IService<TOrder> {

    Object ordersStatus(Integer uid, Integer page, Integer size, String status, Integer count);

    Object listOrder(Integer cid, Integer type, Integer page, Integer size, String status, Date start, Date end, Integer count);
	
}
