package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
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

    Object listCompany(String uid, Integer page, Integer size, Integer count);

    Object getCompany(String uid, Integer cid);

    Object updateCompany(TCompany company, String uid, Integer cid);

    Object deleteCompany(String uid, Integer cid);

    Object deleteCompanys(String uid);

    Object addCompony(JSONObject jsonObject, Integer uid);

    Object listCompanys(String search, Integer page, Integer size, Integer count);

    Object getCompanyInfo(Integer cid);

    Object getCompanySetUp(Integer cid, Integer page, Integer size, Integer count);

    Object getCompanyCert(Integer cid, Integer page, Integer size, Integer count);

    Object countCompanyCertById(Integer cid, Integer id);

    Object countCompany();

    Object removeSetupState(Integer cid,  Integer setupid);

}
