package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TLogs;
import org.ruoxue.backend.mapper.TLogsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.service.ITLogsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TLogsServiceImpl extends ServiceImpl<TLogsMapper, TLogs> implements ITLogsService {

    @Resource
    TLogsMapper mapper;

    public Boolean actionLog(Integer aid, Date tm, String description, Integer cls) {
        return mapper.actionLog(aid, tm, description, cls);
    }

    public Boolean actionLogNow(Integer aid, String description, Integer cls) {
        return actionLog(aid, new Date(), description, cls);
    }
}
