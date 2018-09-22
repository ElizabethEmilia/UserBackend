package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TShopItems;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-16
 */
public interface ITShopItemsService extends IService<TShopItems> {

    Object listItems(Integer page, Integer size, Integer count);

    Object addItems(JSONObject jsonObject);

    Object updateItems(JSONObject jsonObject, Integer itemid);

    Object removeItems(Integer itemid);

    Object getItems(Integer itemid);

}
