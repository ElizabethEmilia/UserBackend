package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITShopItemsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-09-16
 */
@Controller
@RequestMapping("/api/items")
public class TShopItemsController {

    @Resource
    private ITShopItemsService shopItemsService;

    @ApiOperation("信息的商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody Object listItems(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return shopItemsService.listItems(page, size, count);
    }

    @ApiOperation("商品的添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Object addItems(@RequestBody JSONObject jsonObject) {
        return shopItemsService.addItems(jsonObject);
    }

    @ApiOperation("商品的修改")
    @RequestMapping(value = "/{itemid}/modify", method = RequestMethod.POST)
    public @ResponseBody Object updateItems(@RequestBody JSONObject jsonObject, @PathVariable Integer itemid) {
        return shopItemsService.updateItems(jsonObject, itemid);
    }

    @ApiOperation("商品的删除")
    @RequestMapping(value = "/{itemid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object removeItems(@PathVariable Integer itemid) {
        return shopItemsService.removeItems(itemid);
    }

    @ApiOperation("商品信息的获取")
    @RequestMapping(value = "/{itemid}", method = RequestMethod.GET)
    public @ResponseBody Object getItems(@PathVariable Integer itemid) {
        return shopItemsService.getItems(itemid);
    }

	
}
