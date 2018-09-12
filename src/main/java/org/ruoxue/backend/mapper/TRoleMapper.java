package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.ruoxue.backend.bean.TRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-09-11
 */
@Mapper
public interface TRoleMapper extends BaseMapper<TRole> {

    @Select("select * from t_role order by id DESC limit #{page}, #{size}")
    List<Map<String, Object>> list(@Param("page") Integer page, @Param("size") Integer size);

    @Select("select * from t_role where id = #{id}")
    TRole getRoleById(@Param("id") Integer id);

    @Delete("delete from t_role where id = #{id}")
    Integer removeRole(@Param("id") Integer id);

    @Update("update t_role set name = #{name}, value = #{value}, remarks = #{remarks} where id = #{id}")
    Integer updateRole(@Param("name") String name, @Param("value") Integer value, @Param("remarks") String remarks, @Param("id") Integer id);

}