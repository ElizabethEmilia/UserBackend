package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TCustomer;

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
public interface TCustomerMapper extends BaseMapper<TCustomer> {

//    获取所有status为3的用户的lid的数组
    List<Integer> getRemoveCustomerLids();

//    删除status为3的客户
    Integer removeCustomer();

    TCustomer getTCustomerByUid(@Param("uid") Integer uid);

    List<Map<String, Object>> listCustomerss(@Param("page") Integer page, @Param("size") Integer size);


}