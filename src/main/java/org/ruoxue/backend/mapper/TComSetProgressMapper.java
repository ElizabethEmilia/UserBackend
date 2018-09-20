package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TComSetProgress;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.ArrayList;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Mapper
public interface TComSetProgressMapper extends BaseMapper<TComSetProgress> {

//    查看公司设立进度
    ArrayList<TComSetProgress> getSetUp(@Param("uid") Integer uid, @Param("cid") Integer cid);

//    删除一个公司设立进度
    Integer deleteSetUp(@Param("uid") Integer uid, @Param("cid") Integer cid);

//    根据uid和cid获取公司最后一个进度status
    String getStatusByLast(@Param("uid") Integer uid, @Param("cid") Integer cid);

}