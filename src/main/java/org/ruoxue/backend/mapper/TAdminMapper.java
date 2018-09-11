package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.ruoxue.backend.bean.TAdmin;
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
public interface TAdminMapper extends BaseMapper<TAdmin> {

    List<Map<String, Object>> getAdminList(@Param("page") Integer page, @Param("size") Integer size);

//    获取所有status为3的管理员的lid的数组
    List<Integer> getRemoveAdminLids();

//    删除status为3的管理员
    Integer removeAdmin();

    TAdmin getTAdminByUid(@Param("id") Integer id);

    List<TCustomer> listCustomer(@Param("page") Integer page, @Param("size") Integer size);

    Integer updateAdmin(@Param("name") String name, @Param("id") Integer id);

    @Select("select count(1) from t_admin where name = #{name}")
    Integer countAdminByName(@Param("name") String name);

    @Select("select count(1) from t_customer where name = #{name}")
    Integer countCustomerByName(@Param("name") String name);

    @Delete("delete from t_admin where id = #{id}")
    Integer deleteAdmin(@Param("id") Integer id);

    @Select("select * from t_admin where id = #{id}")
    TAdmin getAdminByAid(@Param("id") Integer id);

    @Update("update t_admin set name = #{name}, roleid = #{roleid} where id = #{id}")
    Integer updateAdmin(@Param("id") Integer id, @Param("name") String name, @Param("roleid") Integer roleid);

}