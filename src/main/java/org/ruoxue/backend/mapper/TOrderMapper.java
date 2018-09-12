package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TOrder;

import java.util.Date;
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
public interface TOrderMapper extends BaseMapper<TOrder> {

    List<TOrder> listOrderByStatus(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status);

    List<Map<String, Object>> listOrder(@Param("cid") Integer cid, @Param("type") Integer type, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

}