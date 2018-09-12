package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-11
 */
public interface ITRoleService extends IService<TRole> {

    Object list(Integer page, Integer size);

    Object roleAdd(JSONObject jsonObject);

    Object getRole(Integer roleid);

    Object removeRole(Integer roleid);

    Object updateRole(JSONObject jsonObject, Integer roleid);

}
