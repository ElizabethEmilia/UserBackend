package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.ruoxue.backend.bean.TGroup;

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
public interface TGroupMapper extends BaseMapper<TGroup> {

//    查询组的列表
    @Select("select * from t_group order by id DESC limit #{page}, #{size}")
    List<Map<String, Object>> listGroup(@Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_group")
    Integer countListGroup();

    @Select("select id, name from t_group order by id desc")
    List<Map<String, Object>> listSimple();

    @Delete("delete from t_group where id = #{id}")
    Integer removeGroup(@Param("id") Integer id);

    @Update("update t_group set name = #{name}, remark = #{remark} where id = #{id}")
    Integer updateGroup(@Param("id") Integer id, @Param("name") String name, @Param("remark") String remark);

    @Select("select * from t_admin where gid = #{gid} order by id desc limit #{page}, #{size}")
    List<Map<String, Object>> listGroupAdmin(@Param("gid") Integer gid, @Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_admin where gid = #{gid}")
    Integer countListGroupAdmin(@Param("gid") Integer gid);

    @Select("select * from t_customer where aid in (select * from (select id from t_admin where gid = #{gid}) c) order by uid desc limit #{page}, #{size}")
    List<Map<String, Object>> listGroupCustomer(@Param("gid") Integer gid, @Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_customer where aid in (select * from (select id from t_admin where gid = #{gid}) c)")
    Integer countistGroupCustomer(@Param("gid") Integer gid);

}