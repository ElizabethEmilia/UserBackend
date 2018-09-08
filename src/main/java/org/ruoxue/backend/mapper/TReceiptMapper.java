package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TReceipt;

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
public interface TReceiptMapper extends BaseMapper<TReceipt> {

//      查看客户公司的开票申请
    List<Map<String, Object>> listReceipt(@Param("uid") String uid, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size,
                                          @Param("type") Integer type, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

//      根据uid和rid获取receipt实体
    TReceipt getReceipt(@Param("uid") Integer uid, @Param("id") Integer id);

}