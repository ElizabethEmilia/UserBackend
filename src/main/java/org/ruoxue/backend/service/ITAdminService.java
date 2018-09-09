package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TAdmin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITAdminService extends IService<TAdmin> {

    Object basicGet(Integer uid);

    Object basicPost(JSONObject jsonObject);

    Object password(String old_pwd, String new_pwd);
	
}
