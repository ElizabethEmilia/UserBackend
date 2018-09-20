package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TTaxAccount;

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
public interface TTaxAccountMapper extends BaseMapper<TTaxAccount> {

//    统计税金账户余额,
    List<Map<String, Object>> listGroup(@Param("page") Integer page, @Param("size") Integer size, @Param("cid") Integer cid, @Param("yfrom") Date yfrom, @Param("yto") Date yto, @Param("uid") Integer uid);

//    根据年份和cid查询最大的那一条
    @Select("select pre_tax_ratio from t_expected_income where uid=#{uid} and cid = #{cid} and tm_op like concat('%', #{time}, '%') order by tm_op desc limit 1")
    Double getRatioByTimeAndCid(@Param("cid") Integer cid, @Param("time") String time, @Param("uid") Integer uid);

//    税金账户明细
    List<Map<String, Object>> listTaxDetail(@Param("page") Integer page, @Param("size") Integer size, @Param("cid") Integer cid, @Param("mfrom") Date mfrom, @Param("mto") Date mto, @Param("uid") Integer uid);

}