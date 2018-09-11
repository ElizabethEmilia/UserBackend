package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TPublicCharge;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITPublicChargeService extends IService<TPublicCharge> {

    Object updatePublicchargeStatus(Integer uid, Integer pid, String status);
	
}
