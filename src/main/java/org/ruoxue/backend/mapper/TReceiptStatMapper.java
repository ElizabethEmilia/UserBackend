package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TReceiptStat;

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
public interface TReceiptStatMapper extends BaseMapper<TReceiptStat> {

//    获取客户各个公司的开票统计
    List<TReceiptStat> listReceiptStat(@Param("uid") Integer uid);

}