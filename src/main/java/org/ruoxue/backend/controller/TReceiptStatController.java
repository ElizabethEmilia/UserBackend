package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITReceiptStatService;
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
public class TReceiptStatController {

    @Resource
    private ITReceiptStatService receiptStatService;

    @ApiOperation("查看客户各个公司的开票统计")
    @RequestMapping(value = "/{uid}/receipt/stat", method = RequestMethod.GET)
    public @ResponseBody Object receiptStat(@PathVariable String uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return receiptStatService.receiptStat(uid, page, size, count);
    }

    @ApiOperation("查看客户各个公司的开票统计")
    @RequestMapping(value = "/{uid}/receipt/statv", method = RequestMethod.GET)
    public @ResponseBody Object receiptStatV(@PathVariable String uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        return receiptStatService.receiptStat(uid, page, size, count);
    }
	
}
