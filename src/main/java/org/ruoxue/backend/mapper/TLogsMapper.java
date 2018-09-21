package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TLogs;

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
public interface TLogsMapper extends BaseMapper<TLogs> {

    @Insert("insert into t_logs (aid, tm, description, cls) values(#{aid},#{tm},#{description},#{cls})")
    Boolean actionLog(Integer aid, Date tm, String description, Integer cls);

//    查询日志的列表
    @Select("(select t_logs.*, t_admin.name from t_logs ,t_admin where t_logs.aid=t_admin.id ) union (select t_logs.*, '系统' as name from t_logs where t_logs.aid=-1 ) order by id DESC limit #{page}, #{size}")
    List<Map<String, Object>> listLogs(@Param("page") Integer page, @Param("size") Integer size);

//    描述 + 类名
    @Insert("insert into t_logs value(null, #{aid}, now(), #{desc}, #{cls})")
    Integer addLog(@Param("aid") Integer aid, @Param("desc") String desc, @Param("cls") Integer cls);

}
