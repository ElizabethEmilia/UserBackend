package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TDictProvinces;

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
public interface TDictProvincesMapper extends BaseMapper<TDictProvinces> {

//    获取所有的省份
    List<String> getProvince();

//    根据省名获取省份的code
    Integer getProcinceCodeByName(@Param("name") String name);

}