package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TComCert;
import org.ruoxue.backend.bean.TCompany;

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

    //    获取公司列表
    List<Map<String, Object>> listCompanys(@Param("search") String search, @Param("page") Integer page, @Param("size") Integer size);

    //    获取一个公司的信息
    @Select("select * from t_company where id = #{id}")
    TCompany getCompanyById(@Param("id") Integer id);

    //    获取一个公司的设立进度，并按时间排序
    @Select("select * from t_com_set_progress where cid = #{cid} order by tm desc limit #{page},#{size}")
    List<Map<String, Object>> listCompanySetUp(@Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size);

    //    获取一个公司的证件照
    @Select("select * from t_com_cert where cid = #{cid} limit #{page},#{size}")
    List<Map<String, Object>> listComCertByCid(@Param("cid") Integer cid, @Param("page") Integer page, @Param("size") Integer size);

    //    获取一个公司的证件照信息
    @Select("select * from t_com_cert where cid = #{cid} and id = #{id}")
    TComCert getComCertById(@Param("cid") Integer cid, @Param("id") Integer id);

    //    获取该用户拥有的公司数量
    @Select("select count(1) num from t_company where uid = #{uid}")
    Integer countCompanyByUid(@Param("uid") Integer uid);

//    修改服务到期时间
    @Update("update t_company set tax_pack_end = #{tax_pack_end} where id = #{id}")
    Integer updateEndTime(@Param("id") Integer id, @Param("tax_pack_end") Date tax_pack_end);

//    获取所有用户所有公司的列表
    @Select("select * from t_company order by id desc")
    List<Map<String, Object>> listComAll();

//    更新所有公司预选状态为0
    @Update("update t_company set ysa_status = 0")
    Integer updateStatus();

}