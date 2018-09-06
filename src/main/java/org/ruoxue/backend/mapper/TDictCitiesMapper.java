package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TDictCities;

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
public interface TDictCitiesMapper extends BaseMapper<TDictCities> {

//    根据省份id获取市级
    List<String> getCityByProvinceId(@Param("province_id") Integer province_id);

//    根据市名获取市的code
    Integer getCityCodeByName(@Param("name") String name);


}