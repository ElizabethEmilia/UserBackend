package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Mapper
public interface MainMapper {

//    通过用户名获取管理员
    TAdmin getTAdminByName(@Param("name") String name);

//    通过用户名获取客户
    TCustomer getTCustomerByName(@Param("name") String name);

}