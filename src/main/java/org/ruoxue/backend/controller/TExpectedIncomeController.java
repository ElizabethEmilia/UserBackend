package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITExpectedIncomeService;
import org.ruoxue.backend.util.XunBinKit;
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
public class TExpectedIncomeController {

    @Resource
    private ITExpectedIncomeService expectedIncomeService;

    @ApiOperation("获取最近交易信息（含收入和支出）")
    @RequestMapping(value = "/exchange/recent", method = RequestMethod.GET)
    public @ResponseBody Object exchangeByRecent() {
        return expectedIncomeService.exchangeByRecent();
    }

    @ApiOperation("获取钱包记录")
    @RequestMapping(value = "/exchange/{type}", method = RequestMethod.GET)
    public @ResponseBody Object listExchangeByType(@PathVariable String type, @RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Date start, @RequestParam(required = false) Date end, @RequestParam(required = false) Integer count) {
        return expectedIncomeService.listExchangeByType(type, cid, page, size, start, end, count);
    }

    /**
     *  年销售额预选(客户操作)
     */
    @ApiOperation("列出当年的税金选择操作历史记录表")
    @RequestMapping(value = "/company/{cid}/sales/list/current", method = RequestMethod.GET)
    public @ResponseBody Object listExpectIncomeByYear(@PathVariable Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return expectedIncomeService.listExpectIncomeByYear(cid, page, size, count);
    }

    @ApiOperation("列出所有的税金操作记录表")
    @RequestMapping(value = "/company/{cid}/sales/list/all", method = RequestMethod.GET)
    public @ResponseBody Object listExpectIncome(@PathVariable Integer cid, @RequestParam(required = false) Integer status, @RequestParam(required = false) Date from,
                                                       @RequestParam(required = false) Date to, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return expectedIncomeService.listExpectIncome(cid, status, from, to, page, size, count);
    }

    @ApiOperation("获取最后预选一条记录")
    @RequestMapping(value = "/company/{cid}/sales/last", method = RequestMethod.GET)
    public @ResponseBody Object lastExpectTime(@PathVariable Integer cid) {
        return expectedIncomeService.lastExpectTime(cid);
    }

    @ApiOperation("选择年销售额范围（选择大范围）")
    @RequestMapping(value = "/company/{cid}/sales/preselect", method = RequestMethod.POST)
    public @ResponseBody Object preSelect(@PathVariable Integer cid, @RequestParam Integer ysaRange) {
        return expectedIncomeService.preSelect(cid, ysaRange);
    }

    @ApiOperation("【管理员】变更年销售额范围 （大范围）")
    @RequestMapping(value = "/customer/_/company/{cid}/sales/reselect", method = RequestMethod.POST)
    public @ResponseBody Object reselect(@PathVariable Integer cid, @RequestParam Integer ysaRange) {
        if (XunBinKit.shouldReject(PermissionManager.Moudles.ModifyExpectedIncomeForCustomer));
        return expectedIncomeService.preSelect(cid, ysaRange);
    }

    @ApiOperation("变更年销售额范围（选择小范围）")
    @RequestMapping(value = "/company/{cid}/sales/complement", method = RequestMethod.POST)
    public @ResponseBody Object complement(@PathVariable Integer cid) {
        return expectedIncomeService.complement(cid);
    }

    @ApiOperation("撤回变更")
    @RequestMapping(value = "/company/{cid}/sales/withdraw", method = RequestMethod.POST)
    public @ResponseBody Object withdraw(@PathVariable Integer cid) {
        return expectedIncomeService.withdraw(cid);
    }

    /**
     *  年销售额预选(管理员操作)
     */
    @ApiOperation("列出当年的税金选择操作历史记录表(管理员)")
    @RequestMapping(value = "/customer/{uid}/company/{cid}/sales/list/current", method = RequestMethod.GET)
    public @ResponseBody Object listAdminCurrentByYear(@PathVariable Integer cid, @PathVariable String uid,
                                @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return expectedIncomeService.listAdminCurrentByYear(cid, uid, page, size, count);
    }

    @ApiOperation("列出所有的税金操作记录表(管理员)")
    @RequestMapping(value = "/customer/{uid}/company/{cid}/sales/list/all", method = RequestMethod.GET)
    public @ResponseBody Object listAdminCurrent(@PathVariable Integer cid, @RequestParam(required = false) Integer status, @RequestParam(required = false) Date from, @RequestParam(required = false) Date to,
                                @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @PathVariable String uid, @RequestParam(required = false) Integer count) {
        return expectedIncomeService.listAdminCurrent(cid, status, from, to, page, size, uid, count);
    }

    @ApiOperation("手动更改税金状态")
    @RequestMapping(value = "/customer/{uid}/company/{cid}/sales/{action}", method = RequestMethod.POST)
    public @ResponseBody Object updateStatusByAction(@PathVariable Integer cid, @PathVariable String uid, @PathVariable String action) {
        return expectedIncomeService.updateStatusByAction(cid, uid, action);
    }




}
