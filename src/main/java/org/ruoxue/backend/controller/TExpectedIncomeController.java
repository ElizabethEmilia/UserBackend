package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITExpectedIncomeService;
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
@RequestMapping("/api/exchange")
public class TExpectedIncomeController {

    @Resource
    private ITExpectedIncomeService expectedIncomeService;

    @ApiOperation("获取最近交易信息（含收入和支出）")
    @RequestMapping(value = "/recent", method = RequestMethod.GET)
    public @ResponseBody Object exchangeByRecent() {
        return expectedIncomeService.exchangeByRecent();
    }

    @ApiOperation("获取钱包记录")
    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public @ResponseBody Object listExchangeByType(@PathVariable String type, @RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Date start, @RequestParam(required = false) Date end) {
        return expectedIncomeService.listExchangeByType(type, cid, page, size, start, end);
    }


}
