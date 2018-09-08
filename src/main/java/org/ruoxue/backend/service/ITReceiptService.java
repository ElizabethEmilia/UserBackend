package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TReceipt;
import com.baomidou.mybatisplus.service.IService;

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

    Object receiptRequest(String uid, Integer rid, String action);

}
