package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITExchangeService;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
public class TExchangeController {

    @Resource
    private ITExchangeService exchangeService;

    @ApiOperation("查看客户的充值记录")
    @RequestMapping(value = "/{uid}/onlinecharge", method = RequestMethod.GET)
    public @ResponseBody Object onlinecharge(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return exchangeService.onlinecharge(uid, page, size);
    }

    @ApiOperation("查看客户的对公充值记录(分类)")
    @RequestMapping(value = "/{uid}/publiccharge/{status}", method = RequestMethod.GET)
    public @ResponseBody Object publicchargeStatus(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String status) {
        return exchangeService.publicchargeStatus(uid, page, size, status);
    }


	
}
