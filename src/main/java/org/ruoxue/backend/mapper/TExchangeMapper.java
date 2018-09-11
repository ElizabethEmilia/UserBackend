package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TPublicCharge;

import java.math.BigDecimal;
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

//    总收入
    BigDecimal countIncome(@Param("uid") Integer uid);

//    总支出
    BigDecimal countOutcome(@Param("uid") Integer uid);

//    上次收入
    BigDecimal countLastIncome(@Param("uid") Integer uid);

    BigDecimal countLastOutcome(@Param("uid") Integer uid);

}