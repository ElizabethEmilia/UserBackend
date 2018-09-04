package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TAdmin;

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

    List<Map<String, Object>> getAdminList(@Param("page") Integer page, @Param("size") Integer size);

//    获取所有status为3的管理员的lid的数组
    List<Integer> getRemoveAdminLids();

//    删除status为3的管理员
    Integer removeAdmin();


}