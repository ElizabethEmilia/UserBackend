package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.ruoxue.backend.bean.TReceipt;

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
public interface TReceiptMapper extends BaseMapper<TReceipt> {

//      查看客户公司的开票申请
    List<Map<String, Object>> listReceipt(@Param("uid") String uid, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size,
                                          @Param("type") Integer type, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

//      开票情况统计的查看统计数据功能
    List<Map<String, Object>> listTaxStat(@Param("uid") Integer uid, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size,
                                          @Param("type") Integer type, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end);

    //      根据uid和rid获取receipt实体
    TReceipt getReceipt(@Param("uid") Integer uid, @Param("id") Integer id);

//    开票列表
    List<Map<String, Object>> receiptList(@Param("cid") Integer cid, @Param("type") Integer type, @Param("page") Integer page, @Param("size") Integer size, @Param("status") Integer status, @Param("start") Date start, @Param("end") Date end, @Param("uid") Integer uid);

    @Update("update t_receipt set status = #{status} where id = #{id}")
    Integer updateStatusToSub(@Param("status") Integer status, @Param("id") Integer id);

//    根据rid获取receipt实体
    @Select("select * from t_receipt where id = #{id}")
    TReceipt getReceiptById(@Param("id") Integer id);

    @Delete("delete from t_receipt where id = #{id}")
    Integer removeReceipt(@Param("id") Integer id);

    @Select("select * from t_receipt limit 1000")
    List<Map<String, Object>> listReceiptAll();

    @Select("select * from t_receipt where uid=#{uid} limit 1000")
    List<Map<String, Object>> listReceiptAllByUID(@Param("uid") Integer uid);

//    查看开票统计(***)
    @Select("select a.cid, b.name  from t_receipt a, t_company b where b.uid=#{uid} and a.cid = b.id and b.tm_first_ec is not null group by a.cid order by a.cid desc limit #{page}, #{size}")
    List<Map<String, Object>> statReceipt(@Param("page") Integer page, @Param("size") Integer size, @Param("uid") Integer uid);

//    12个月收入之和
    @Select("select sum(rec_amount) amountSum from t_receipt where cid = #{cid} and tm_vallidate > #{start} and tm_vallidate < #{end}")
    Double getYear(@Param("cid") Integer cid, @Param("start") Date start, @Param("end") Date end);

//    当月普票次数
    @Select("select count(1) from t_receipt where rec_type = 0 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) group by date(tm_vallidate)")
    List<Integer> countTimePTickets(@Param("cid") Integer cid);

//    当月专票次数
    @Select("select count(1) from t_receipt where rec_type = 1 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) group by date(tm_vallidate)")
    List<Integer> countTimeZTickets(@Param("cid") Integer cid);

//    当月普票张数
    @Select("select count(1) from t_receipt where rec_type = 0 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
    Integer countPTickets(@Param("cid") Integer cid);

//    当月当月普票金额
    @Select("select sum(rec_amount) from t_receipt where rec_type = 0 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
    Double countMonthPTicets(@Param("cid") Integer cid);

//    当月专票张数
    @Select("select count(1) from t_receipt where rec_type = 1 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
    Integer countZTickets(@Param("cid") Integer cid);

//    当月专票金额
    @Select("select sum(rec_amount) from t_receipt where rec_type = 1 and cid = #{cid} and DATE_FORMAT( tm_vallidate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
    Double countMonthZTicets(@Param("cid") Integer cid);
}