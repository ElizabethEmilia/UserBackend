package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TReceiptStat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITReceiptStatService extends IService<TReceiptStat> {

    Object receiptStat(String uid, Integer page, Integer size, Integer count);

    Object receiptStatV(String uid);

}
