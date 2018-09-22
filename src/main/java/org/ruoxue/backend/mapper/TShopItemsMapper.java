package org.ruoxue.backend.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ruoxue.backend.bean.TShopItems;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author fengjb
 * @since 2018-09-16
 */
public interface TShopItemsMapper extends BaseMapper<TShopItems> {

//    获取商品信息列表
    @Select("select * from t_shop_items order by id DESC limit #{page}, #{size}")
    List<Map<String, Object>> list(@Param("page") Integer page, @Param("size") Integer size);

    @Select("select count(1) from t_shop_items")
    Integer countList();

//    通过商品id获取商品
    @Select("select * from t_shop_items where id = #{id}")
    TShopItems getItemById(@Param("id") Integer id);

//    删除一件商品
    @Delete("delete from t_shop_items where id = #{id}")
    Integer removeItem(@Param("id") Integer id);

//    修改一个商品
    @Update("update t_shop_items set name = #{name}, price = #{price}, description = #{description}, image = #{image}, addyear = #{addyear} where id = #{id}")
    Integer updateItem(@Param("name") String name, @Param("price") Double price, @Param("description") String description, @Param("image") String image, @Param("addyear") Integer addyear, @Param("id") Integer id);

//    通过name查询一个商品
    @Select("select * from t_shop_items where name = #{name} order by id desc limit 1")
    TShopItems getItemByName(@Param("name") String name);

//    修改商品年限
    @Update("update t_shop_items set addyear = #{addyear} where id = #{id}")
    Integer updateAddYear(@Param("addyear") Integer addyear, @Param("id") Integer id);


}