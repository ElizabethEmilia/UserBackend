package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TGroup;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-11
 */
public interface ITGroupService extends IService<TGroup> {

    Object listGroup(Integer page, Integer size, Integer count);

    Object listSimple();

    Object groupAdd(JSONObject jsonObject);

    Object groupRemove(Integer gid);

    Object groupUpdate(Integer gid, String name, String remark);

    Object listGroupAdmin(Integer gid, Integer page, Integer size, Integer count);

    Object listGroupCustomer(Integer gid, Integer page, Integer size, Integer count);

}
