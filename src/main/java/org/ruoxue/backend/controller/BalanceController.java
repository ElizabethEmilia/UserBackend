package org.ruoxue.backend.controller;

import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITTaxAccountDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  账户余额控制器
 * @author fengjb
 * @date 2018-09-23
 */
@Controller
@RequestMapping("/customer")
public class BalanceController {

    @Resource
    private ITTaxAccountDetailService taxAccountDetailService;

    @ApiOperation("后台手动为客户充值")
    @RequestMapping(value = "/{uid}/charge/{dst}", method = RequestMethod.POST)
    public @ResponseBody Object RechargeMoney (@PathVariable Integer uid, @PathVariable String dst, @RequestParam Double amount) {
        return taxAccountDetailService.RechargeMoney(uid, dst, amount);
    }

    @ApiOperation("后台手动填写扣费金额并扣费")
    @RequestMapping(value = "/{uid}/deduction/{dst}", method = RequestMethod.POST)
    public @ResponseBody Object deductionDst (@PathVariable Integer uid, @PathVariable String dst, @RequestParam Double amount,
               @RequestParam(required = false) Integer cid, @RequestParam(required = false) String credit, @RequestParam Boolean deduced) {
        return taxAccountDetailService.deductionDst(uid, dst, amount, cid, credit, deduced);
    }



}
