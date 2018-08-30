package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.base.TOperationLog;
import org.ruoxue.backend.mapper.TOperationLogMapper;
import org.ruoxue.backend.service.ITOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TOperationLogServiceImpl extends ServiceImpl<TOperationLogMapper, TOperationLog> implements ITOperationLogService {
	
}
