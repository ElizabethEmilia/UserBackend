package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TReceipt;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITReceiptService extends IService<TReceipt> {

    Object listReceipt(String uid, Integer cid, Integer page, Integer size, Integer type, Integer status, Date start, Date end);

    Object listTaxStat(Integer cid, Integer page, Integer size, Integer status, Integer type, Date start, Date end);

    Object updateReceiptStat(Integer uid, Integer cid, JSONObject jsonObject);

    Object listReceiptStatUpdate(Integer uid, Integer page, Integer size);

    Object receiptRequest(String uid, Integer rid, String action, String reason);

    Object receiptAdd(JSONObject jsonObject);

    Object receiptList(Integer page, Integer size, Integer cid, Integer type, Integer status, Date start, Date end);

    Object removeReceipt(Integer rid);

    Object exportReceipt();

    Object statReceipt(Integer page, Integer size);

    Object updateStatusToSub(Integer rid);

    Object statReceiptVuser();

}
