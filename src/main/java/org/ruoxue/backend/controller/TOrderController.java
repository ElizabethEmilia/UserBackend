package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api/customer")
public class TOrderController {

    @Resource
    private ITOrderService orderService;

    @ApiOperation("查看客户的对公充值记录(分类)")
    @RequestMapping(value = "/{uid}/orders/{status}", method = RequestMethod.GET)
    public @ResponseBody
    Object ordersStatus(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String status) {
        return orderService.ordersStatus(uid, page, size, status);
    }
	
}
