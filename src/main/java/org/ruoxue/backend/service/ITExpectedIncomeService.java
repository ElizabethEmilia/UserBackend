package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TExpectedIncome;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITExpectedIncomeService extends IService<TExpectedIncome> {

    Object exchangeByRecent();

    Object listExchangeByType(String type, Integer cid, Integer page, Integer size, Date start, Date end);
	
}
