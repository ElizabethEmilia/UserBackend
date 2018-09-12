package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api")
public class TOrderController {

    @Resource
    private ITOrderService orderService;

    @ApiOperation("查看客户的对公充值记录(分类)")
    @RequestMapping(value = "/customer/{uid}/orders/{status}", method = RequestMethod.GET)
    public @ResponseBody
    Object ordersStatus(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String status) {
        return orderService.ordersStatus(uid, page, size, status);
    }

    @ApiOperation("获取产品订单信息")
    @RequestMapping(value = "/order/{status}", method = RequestMethod.GET)
    public @ResponseBody Object listOrder(@RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String status, @RequestParam(required = false) Date start, @RequestParam(required = false) Date end) {
        return orderService.listOrder(cid, type, page, size, status, start, end);
    }
	
}
