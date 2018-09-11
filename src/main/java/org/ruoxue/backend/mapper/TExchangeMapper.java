package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TPublicCharge;

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
public interface TExchangeMapper extends BaseMapper<TExchange> {

    @Select("select * from t_exchange where uid = #{uid} order by id DESC limit #{page}, #{size}")
    List<TExchange> getOnlinecharge(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size);

    List<TPublicCharge> getPublicCharge(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status);

}