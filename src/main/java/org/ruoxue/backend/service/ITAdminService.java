package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.base.TOperationLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITAdminService extends IService<TAdmin> {

    Object handleAdminAdd(TAdmin admin);

    Object handleAdminUpdate(TAdmin admin);

    Object handleAdminRemove(Integer id);

    List<Map<String, Object>> getAdminList(Page<TOperationLog> page);
	
}
