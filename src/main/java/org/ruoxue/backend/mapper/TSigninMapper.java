package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TSignin;

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
public interface TSigninMapper extends BaseMapper<TSignin> {

//    根据uid获取sign实体
    TSignin getSigninByUid(@Param("id") Integer id);

//    删除多条记录
    Integer removeSign(@Param("id") Integer id);

    Integer updatePassword(@Param("password") String password, @Param("id") Integer id);


}