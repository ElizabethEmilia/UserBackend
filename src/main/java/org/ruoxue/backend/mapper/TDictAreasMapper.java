package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TDictAreas;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
@Mapper
public interface TDictAreasMapper extends BaseMapper<TDictAreas> {

//    获取市下所有的区
    List<String> getDistrictByCityId(@Param("city_id") Integer city_id);

}