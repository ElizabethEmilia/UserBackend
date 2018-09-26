package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoxue.backend.bean.TPending;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-09-22
 */
public interface TPendingMapper extends BaseMapper<TPending> {

    @Select("select * from t_pending where uid = #{uid} and receiver = #{rece} and processed = #{proce} and senderaid <> #{senderaid}")
    List<Map<String, Object>> getNotificationByUid(@Param("uid") Integer uid, @Param("rece") Integer rece, @Param("proce") Integer proce, @Param("senderaid") Integer senderaid);

    @Select("select * from t_pending where gid = #{gid} and receiver = #{rece} and processed = #{proce} and senderaid <> #{senderaid}")
    List<Map<String, Object>> getNotificationByGid(@Param("gid") Integer gid, @Param("rece") Integer rece, @Param("proce") Integer proce, @Param("senderaid") Integer senderaid);

    @Select("select * from t_pending where aid = #{aid} and receiver = #{rece} and processed = #{proce} and senderaid <> #{senderaid}")
    List<Map<String, Object>> getNotificationByAid(@Param("aid") Integer aid, @Param("rece") Integer rece, @Param("proce") Integer proce, @Param("senderaid") Integer senderaid);

    @Select("select * from t_pending where receiver = #{rece} and processed = #{proce} and senderaid <> #{senderaid}")
    List<Map<String, Object>> getNotification(@Param("rece") Integer rece, @Param("proce") Integer proce, @Param("senderaid") Integer senderaid);

    @Delete("delete from t_pending where id = #{id}")
    Integer removeNoto(@Param("id") Integer id);

    @Insert("insert into t_pending (uid, description, receiver, senderaid, tm) values(#{uid}, #{desc}, 0, -1, #{tm})")
    Boolean sendNotificationToCustomer(@Param("uid") Integer uid, @Param("desc") String desc, @Param("tm") Date time);

}