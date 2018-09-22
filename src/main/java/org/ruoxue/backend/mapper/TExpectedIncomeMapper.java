package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TExpectedIncome;

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
public interface TExpectedIncomeMapper extends BaseMapper<TExpectedIncome> {

    List<TExchange> listExchange(@Param("type") Integer type, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size, @Param("start") Date start, @Param("end") Date end, @Param("uid") Integer uid);

    Integer countListExchange(@Param("type") Integer type, @Param("cid") Integer cid, @Param("start") Date start, @Param("end") Date end, @Param("uid") Integer uid);

//    客户
    List<Map<String, Object>> listExpectIncomeByYear(@Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_expected_income where tm_inactivate like concat('%', year(curdate()), '%') and cid = #{cid}")
    Integer countListExpectIncomeByYear(@Param("cid") Integer cid);

    List<Map<String, Object>> listExpectIncome(@Param("cid") Integer cid, @Param("status") Integer status, @Param("page") Integer page, @Param("size") Integer size, @Param("from") Date from, @Param("to") Date to);

    Integer countListExpectIncome(@Param("cid") Integer cid, @Param("status") Integer status, @Param("from") Date from, @Param("to") Date to);

    @Select("select * from t_expected_income where cid = #{cid} order by tm_activate desc limit 1")
    TExpectedIncome getExpectLast(@Param("cid") Integer cid);

//    管理员
    List<Map<String, Object>> listAdminCurrentByYear(@Param("cid") Integer cid, @Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size);

    Integer countListAdminCurrentByYear(@Param("cid") Integer cid, @Param("uid") Integer uid);

    List<Map<String, Object>> listAdminCurrent(@Param("cid") Integer cid, @Param("uid") Integer uid, @Param("status") Integer status, @Param("page") Integer page, @Param("size") Integer size, @Param("from") Date from, @Param("to") Date to);

    Integer countListAdminCurrent(@Param("cid") Integer cid, @Param("uid") Integer uid, @Param("status") Integer status, @Param("from") Date from, @Param("to") Date to);

    TExpectedIncome getExceptByUidAndCid(@Param("uid") Integer uid, @Param("cid") Integer cid);

//    更改状态
    @Update("update t_expected_income set status = #{status} where id = #{id}")
    Integer updateExpectById(@Param("status") Integer status, @Param("id") Integer id);
}