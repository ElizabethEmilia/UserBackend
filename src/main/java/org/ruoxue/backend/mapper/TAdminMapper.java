package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.base.TOperationLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Mapper
public interface TAdminMapper extends BaseMapper<TAdmin> {

    List<Map<String, Object>> getAdminList(@Param("page") Page<TOperationLog> page, @Param("orderByField")String orderByField, @Param("isAsc") boolean asc);

}