package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    // 通过用户名或手机号获取用户
    @Select("select * from t_customer where name=#{param} or phone=#{param} limit 1")
    TCustomer getCustomerByNameOrPhone(@Param("param") String param);

    // 通过用户名或手机号获取管理员
    @Select("select * from t_admin where name=#{param} or phone=#{param} limit 1")
    TAdmin getAdminByNameOrPhone(@Param("param") String param);

    // 判断号码是否存在
    @Select("select " +
            "(select count(*) from t_admin where phone=#{phone}) + " +
            "(select count(*) from t_customer where phone=#{phone}) > 0 as _exists " +
            "from dual")
    Integer testIfPhoneNumberExists(@Param("phone") String phone);
}