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

    Object listExchangeByType(String type, Integer cid, Integer page, Integer size, Date start, Date end, Integer count);

    Object listExpectIncomeByYear(Integer cid, Integer page, Integer size, Integer count);

    Object listExpectIncome(Integer cid, Integer status, Date from, Date to, Integer page, Integer size, Integer count);

    Object lastExpectTime(Integer cid);

    Object preSelect(Integer cid, Integer ysaRange);

    Object reselect(Integer cid, Integer ysaRange);

    Object complement(Integer cid);

    Object withdraw(Integer cid);


    Object listAdminCurrentByYear(Integer cid, String uid, Integer page, Integer size, Integer count);

    Object listAdminCurrent(Integer cid, Integer status, Date from, Date to, Integer page, Integer size, String uid, Integer count);

    Object updateStatusByAction(Integer cid, String uid, String action);
	
}
