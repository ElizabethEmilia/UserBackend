package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ruoxue.backend.bean.base.TOperationLog;

/**
 * <p>
  * 操作日志 Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Mapper
public interface TOperationLogMapper extends BaseMapper<TOperationLog> {

}