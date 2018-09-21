package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TReceiptStat;

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
public interface TReceiptStatMapper extends BaseMapper<TReceiptStat> {

//    获取客户各个公司的开票统计
    List<TReceiptStat> listReceiptStat(@Param("uid") Integer uid);

    @Select("select sum(rec_amount) as amount, sum(rec_amount*pretax_ratio) as tax from t_receipt where uid = #{uid} and (status not in ( 0, 9, 10))")
    List<Map<String, Object>> getStats(@Param("uid") Integer uid);

//    获取用户开票统计修改记录
    @Select("select * from t_receipt_stat where uid = #{uid} order by id desc limit #{page}, #{size}")
    List<Map<String, Object>> listReceiptStatPage(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size);

//    通过id更改hash值
    @Update("update t_receipt_stat set hash_original = #{hash} where id = #{id}")
    Integer updateStatHash(@Param("id") Integer id, @Param("hash") String hash);

}