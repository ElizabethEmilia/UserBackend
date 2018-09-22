package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.bean.TComCert;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITComCertService extends IService<TComCert> {

    Object listCert(String uid, Integer cid, Integer page, Integer size, Integer count);

    Object getCert(String uid, Integer certid);

    Object deleteCert(String uid, Integer certid);

    Object addCert(JSONObject jsonObject, String uid, Integer cid);
	
}
