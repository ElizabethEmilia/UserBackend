package org.ruoxue.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoxue.backend.bean.TCompany;
import com.baomidou.mybatisplus.mapper.BaseMapper;

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
public interface TCompanyMapper extends BaseMapper<TCompany> {

//    分页公司列表
    List<Map<String, Object>> listCompany(@Param("uid") Integer uid, @Param("page") Integer page, @Param("size") Integer size);

//    不分页公司列表
    List<Map<String, Object>> listCompanyAll(@Param("uid") Integer uid);

//    客户的某个公司的信息
    TCompany getCompany(@Param("uid") Integer uid, @Param("cid") Integer cid);

//    删除一个公司信息
    Integer deleteCompany(@Param("uid") Integer uid, @Param("cid") Integer cid);

//    删除所有的公司
    Integer deleteCompanys(@Param("uid") Integer uid);

}