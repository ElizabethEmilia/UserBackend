package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
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

    @Select("select count(1) from t_exchange where uid = #{uid}")
    Integer countGetOnlinecharge(@Param("uid") Integer uid);

    List<TPublicCharge> getPublicCharge(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status);

    Integer countGetPublicCharge(@Param("uid") Integer uid, @Param("status") Integer status);

//    总收入
    Double countIncome(@Param("uid") Integer uid);

//    总支出
    Double countOutcome(@Param("uid") Integer uid);

//    上次收入
    Double countLastIncome(@Param("uid") Integer uid);

    Double countLastOutcome(@Param("uid") Integer uid);

    @Insert("insert into t_exchange (uid, amount, paymethod, tm, type, note, state, dst) " +
            "values(#{uid}, #{amount}, #{paymethod}, #{tm}, #{type}, #{note}, #{state}, #{dst})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    int insertReturnsID(TExchange entity);

    @Update("update t_exchange set running=#{running} where id=#{id}")
    boolean updateRunningByID1(@Param("id") int id, @Param("running") long running);

    @Update("update t_exchange set state=#{state} where id=#{id}")
    boolean updateStateByID(@Param("id")int id, @Param("state") int state);

    @Update("update t_exchange set running=#{running} where id=#{id}")
    boolean updateRunningByID(@Param("id") int id,  @Param("running") String running);

    @Select("select * from t_exchange where id=#{id}")
    TExchange getEntityByID(@Param("id") int id);

    @Select("select id from t_exchange order by id desc limit 1")
    Integer getLastID();

    // 获取最后一条交易记录
    /// TODO: 改为仅查询微信
    @Select("select * from t_exchange order by id DESC limit 1")
    TExchange getLastTransaction();
}