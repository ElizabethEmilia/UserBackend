package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TPublicCharge;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Mapper
public interface TPublicChargeMapper extends BaseMapper<TPublicCharge> {

//    修改对公充值记录状态
    @Update("update t_public_charge set status = #{status} where id = #{id}")
    Integer updatePublicChangeStatus(@Param("id") Integer id, @Param("status") Integer status);

    List<TPublicCharge> listPublicCharge(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

    Integer countListPublicCharge(@Param("uid") Integer uid, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

    @Select("select amount from t_public_charge where id=#{id}")
    Double getPublicChargeAmount(@Param("id") Integer id);
}