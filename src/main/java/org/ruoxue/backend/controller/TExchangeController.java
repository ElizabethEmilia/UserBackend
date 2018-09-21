package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITExchangeService;
import org.ruoxue.backend.service.ITPublicChargeService;
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
public class TExchangeController {

    @Resource
    private ITExchangeService exchangeService;

    @Resource
    private ITPublicChargeService publicChargeService;

    @ApiOperation("查看客户的充值记录")
    @RequestMapping(value = "/{uid}/onlinecharge", method = RequestMethod.GET)
    public @ResponseBody Object onlinecharge(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return exchangeService.onlinecharge(uid, page, size, count);
    }

    @ApiOperation("查看客户的对公充值记录(分类)")
    @RequestMapping(value = "/{uid}/publiccharge/{status}", method = RequestMethod.GET)
    public @ResponseBody Object publicchargeStatus(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String status, @RequestParam(required = false) Integer count) {
        return exchangeService.publicchargeStatus(uid, page, size, status, count);
    }

    @ApiOperation("修改客户的对公充值状态(确认/取消)")
    @RequestMapping(value = "/{uid}/publiccharge/{pid}/{status}", method = RequestMethod.POST)
    public @ResponseBody Object updatePublicchargeStatus(@PathVariable Integer uid, @PathVariable Integer pid, @PathVariable String status) {
        return publicChargeService.updatePublicchargeStatus(uid, pid, status);
    }


	
}
