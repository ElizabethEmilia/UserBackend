package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.ruoxue.backend.bean.TLogs;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Date;

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

}