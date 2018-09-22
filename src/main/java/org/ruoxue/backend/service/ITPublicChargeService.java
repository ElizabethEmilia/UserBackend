package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TPublicCharge;

import java.util.Date;

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

    Object publChargeAdd(JSONObject jsonObject);

    Object publChargeCancel(Integer id);

    Object listPublChargeStatus(Integer page, Integer size, String status, Date start, Date end, Integer count);
	
}
