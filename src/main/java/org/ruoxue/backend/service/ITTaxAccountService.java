package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TTaxAccount;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITTaxAccountService extends IService<TTaxAccount> {

    Object listTaxStat(Integer cid, Date yfrom, Date yto, Integer page, Integer size, Integer count);

    Object listTaxDetail(Integer cid, Date mfrom, Date mto, Integer page, Integer size, Integer count);


    Object listTaxStatAdmin(Integer cid, Date yfrom, Date yto, Integer page, Integer size, Integer count);

    Object listTaxDetailAdmin(Integer cid, Date mfrom, Date mto, Integer page, Integer size, Integer count);

    Object updateTax(Integer cid, Integer uid, Double tax);

}
