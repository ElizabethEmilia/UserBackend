package org.ruoxue.backend.service;

import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TLogs;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITLogsService extends IService<TLogs> {

    Boolean actionLog(Integer aid, Date tm, String description, Integer cls);

    Boolean actionLogNow(Integer aid, String description, Integer cls);

}
