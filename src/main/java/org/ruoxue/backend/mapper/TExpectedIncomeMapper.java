package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TExpectedIncome;

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
public interface TExpectedIncomeMapper extends BaseMapper<TExpectedIncome> {

    List<TExchange> listExchange(@Param("type") Integer type, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size, @Param("start") Date start, @Param("end") Date end);


}