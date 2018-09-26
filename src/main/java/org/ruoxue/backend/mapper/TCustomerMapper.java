package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TCustomer;

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
public interface TCustomerMapper extends BaseMapper<TCustomer> {

//    获取所有status为3的用户的lid的数组
    List<Integer> getRemoveCustomerLids();

//    删除status为3的客户
    Integer removeCustomer();

    TCustomer getTCustomerByUid(@Param("uid") Integer uid);

    List<Map<String, Object>> listCustomerss(@Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_customer where status = 1")
    Integer countListCustomerss();

    @Update("update t_customer " +
            "set name=#{name}, type=#{type}, industry=#{industry}, email=#{email}, " +
            "wechat=#{wechat}, qq=#{qq}, fax=#{fax}, province=#{province}, city=#{city}, district=#{district}, " +
            "address=#{address}, avatar=#{avatar}, other_contact=#{otherContact} " +
            "where uid=#{uid}")
    boolean updateCustomer(TCustomer customer);

    @Update("update t_customer " +
            "set avatar=#{avatar} " +
            "where uid=#{uid}")
    boolean updateAvatar(@Param("avatar") String avatar, @Param("uid") Integer uid);

    @Update("update t_customer set pack_balance = #{balance} where uid = #{id}")
    boolean updateBalance(@Param("balance") Double balance, @Param("id") Integer id);

    @Update("update t_customer set pack_balance = pack_balance+#{balance} where uid = #{id}")
    boolean updatePackBalanceRelative(@Param("balance") Double balance, @Param("id") Integer id);

    @Update("update t_customer set tax_balance = tax_balance+#{balance} where uid = #{id}")
    boolean updateTaxBalanceRelative(@Param("balance") Double balance, @Param("id") Integer id);

    @Update("update t_customer set other_balance = other_balance+#{balance} where uid = #{id}")
    boolean updateOtherBalanceRelative(@Param("balance") Double balance, @Param("id") Integer id);

//    修改服务到期否
    Integer updatePaid(@Param("uid") Integer uid,@Param("paid") Integer paid);

    @Update("update t_customer set checked = #{checked} where uid = #{uid}")
    Integer updateCustomerCheck(@Param("uid") Integer uid, @Param("checked") Integer checked);


//    修改余额(three)
    @Update("update t_customer set pack_balance = #{balance} where uid = #{uid}")
    Integer updatePackBalance(@Param("balance") Double balance, @Param("uid") Integer uid);

    @Update("update t_customer set tax_balance = #{balance} where uid = #{uid}")
    Integer updateTaxBalance(@Param("balance") Double balance, @Param("uid") Integer uid);

    @Update("update t_customer set other_balance = #{balance} where uid = #{uid}")
    Integer updateOtherBalance(@Param("balance") Double balance, @Param("uid") Integer uid);

}