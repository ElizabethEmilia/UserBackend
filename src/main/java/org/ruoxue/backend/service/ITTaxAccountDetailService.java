package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TTaxAccountDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-14
 */
public interface ITTaxAccountDetailService extends IService<TTaxAccountDetail> {

    Object RechargeMoney(Integer uid, String dst, Double amount);

    Object deductionDst(Integer uid, String dst, Double amount, Integer cid, String credit);

}
