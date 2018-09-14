package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TCompany;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITCompanyService extends IService<TCompany> {

    Object listCompany(String uid, Integer page, Integer size);

    Object getCompany(String uid, Integer cid);

    Object updateCompany(TCompany company, String uid, Integer cid);

    Object deleteCompany(String uid, Integer cid);

    Object deleteCompanys(String uid);

    Object addCompony(TCompany company, String uid);

    Object listCompanys(String search, Integer page, Integer size);

    Object getCompanyInfo(Integer cid);

    Object getCompanySetUp(Integer cid, Integer page, Integer size);

    Object getCompanyCert(Integer cid, Integer page, Integer size);

    Object countCompanyCertById(Integer cid, Integer id);

    Object countCompany();

}
