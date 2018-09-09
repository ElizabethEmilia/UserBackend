package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TConfig;

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
public interface TConfigMapper extends BaseMapper<TConfig> {

//    获取keyAndValue
    List<Map<String, Object>> getConfig();

//    获取配置中所有的信息
    List<TConfig> getConfigs();

//    根据key查询value
    String getConfigByName(@Param("name") String name);

//    根据key查询config实体
    TConfig getTConfigByName(@Param("name") String name);

    List<Map<String, Object>> getKeyAndValue();

    Integer updateValueByKey(@Param("name") String name, @Param("value") String value);


}