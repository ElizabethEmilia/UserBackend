package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TArea;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-09-08
 */
public interface TAreaMapper extends BaseMapper<TArea> {

    @Select("select province from t_area group by province")
    List<String> getProvince();

    @Select("select city from t_area where province=#{province} group by city ")
    List<String> getCity(@Param("province") String province);

    @Select("select district from t_area where province=#{province} and city=#{city}")
    List<String> getDistrict(@Param("province") String province, @Param("city") String city);

}