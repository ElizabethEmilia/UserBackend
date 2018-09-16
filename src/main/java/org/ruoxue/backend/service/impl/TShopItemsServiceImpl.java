package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TShopItems;
import org.ruoxue.backend.mapper.TShopItemsMapper;
import org.ruoxue.backend.service.ITShopItemsService;
import org.ruoxue.backend.util.Base64Util;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-16
 */
@Service
public class TShopItemsServiceImpl extends ServiceImpl<TShopItemsMapper, TShopItems> implements ITShopItemsService {

    @Resource
    private TShopItemsMapper shopItemsMapper;

    @Override
    public Object listItems(Integer page, Integer size) {

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }

        if(ToolUtil.isEmpty(size)){
            size = 10;
        }

        page = (page - 1) * size;

        List<Map<String, Object>> listItems = shopItemsMapper.list(page, size);

        return XunBinKit.returnResult(listItems.size() > 0, -2, listItems, "查询成功", "查询失败");

    }

    @Override
    public Object addItems(JSONObject jsonObject) {

//        获取参数
        String name = jsonObject.getString("name");
        Double price = jsonObject.getDouble("price");
        String description = jsonObject.getString("description");
        String image = jsonObject.getString("image");
        Integer addyear = jsonObject.getInteger("addyear");

        if (ToolUtil.isEmpty(name) || ToolUtil.isEmpty(price) || ToolUtil.isEmpty(addyear)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TShopItems shopItems = new TShopItems();
        shopItems.setAddyear(addyear);

        if (ToolUtil.isEmpty(description)) {
            description = "";
        }

        if (ToolUtil.isEmpty(image)) {
            image = "";
            shopItems.setImage(image);
        } else {
//        处理图片
            shopItems.setImage(Base64Util.GenerateImageFromDataURI(image));
        }

        shopItems.setDescription(description);
        shopItems.setName(name);
        shopItems.setPrice(price);
        boolean b = shopItems.insert();

        return XunBinKit.returnResult(b, -2, null, "商品添加成功", "商品添加失败");
    }

    @Override
    public Object updateItems(JSONObject jsonObject, Integer itemid) {

        if (ToolUtil.isEmpty(itemid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TShopItems shopItems = shopItemsMapper.getItemById(itemid);

        if (ToolUtil.isEmpty(shopItems)){
            return ResultUtil.error(-2, "要修改的商品不存在");
        }

//        获取参数
        String name = jsonObject.getString("name");
        Double price = jsonObject.getDouble("price");
        String description = jsonObject.getString("description");
        String image = jsonObject.getString("image");
        Integer addyear = jsonObject.getInteger("addyear");

        if (ToolUtil.isEmpty(name) || ToolUtil.isEmpty(price) || ToolUtil.isEmpty(addyear)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if (ToolUtil.isEmpty(description)) {
            description = "";
        }

        if (ToolUtil.isEmpty(image)) {
            image = "";
        } else {
//        处理图片
            image = Base64Util.GenerateImageFromDataURI(image);
        }

        Integer len = shopItemsMapper.updateItem(name, price, description, image, addyear, itemid);

        return XunBinKit.returnResult(len > 0, -3, null, "修改成功", "修改失败");
    }

    @Override
    public Object removeItems(Integer itemid) {

        if (ToolUtil.isEmpty(itemid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TShopItems shopItems = shopItemsMapper.getItemById(itemid);

        if (ToolUtil.isEmpty(shopItems)){
            return ResultUtil.error(-2, "要删除的商品不存在");
        }

        if (shopItems.getRemovable() == 0) {
            return ResultUtil.error(-3, "该商品无法删除");
        }

        Integer len = shopItemsMapper.removeItem(itemid);

        return XunBinKit.returnResult(len > 0, -4, null, "商品删除成功", "商品删除失败");
    }

    @Override
    public Object getItems(Integer itemid) {

        if (ToolUtil.isEmpty(itemid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TShopItems shopItems = shopItemsMapper.getItemById(itemid);

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(shopItems), -2, shopItems, "查询成功", "查不到此商品");
    }
}
