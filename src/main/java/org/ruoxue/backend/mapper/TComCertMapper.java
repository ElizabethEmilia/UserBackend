package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TComCert;

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
public interface TComCertMapper extends BaseMapper<TComCert> {

//    客户公司的证件列表
    List<Map<String, Object>> listCert(@Param("uid") Integer uid, @Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size);

//    客户公司的某一个证件
    TComCert getCert(@Param("uid") Integer uid, @Param("id") Integer id);

//    删除某一个证件
    Integer deleteCert(@Param("uid") Integer uid, @Param("id") Integer id);

}