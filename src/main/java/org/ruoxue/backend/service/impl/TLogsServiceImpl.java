package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TLogs;
import org.ruoxue.backend.mapper.TLogsMapper;
import org.ruoxue.backend.service.ITLogsService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private TLogsMapper mapper;

    public Boolean actionLog(Integer aid, Date tm, String description, Integer cls) {
        return mapper.actionLog(aid, tm, description, cls);
    }

    public Boolean actionLogNow(Integer aid, String description, Integer cls) {
        return actionLog(aid, new Date(), description, cls);
    }

    @Override
    public Object listLog(Integer page, Integer size, Integer count) {

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(mapper.countListLogs().size());
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = mapper.listLogs(page, size);

        return ResultUtil.success(list);
    }

}
