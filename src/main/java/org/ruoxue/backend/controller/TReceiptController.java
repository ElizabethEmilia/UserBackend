package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITReceiptService;
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
@RequestMapping("/api/customer")
public class TReceiptController {

    @Resource
    private ITReceiptService receiptService;

    @ApiOperation("查看客户公司的开票申请")
    @RequestMapping(value = "/{uid}/receipt", method = RequestMethod.GET)
    public @ResponseBody Object listReceipt(@PathVariable String uid, @RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                            @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer type,
                                            @RequestParam(required = false) Date start, @RequestParam(required = false) Date end){
        return receiptService.listReceipt(uid, cid, page, size, type, status, start, end);
    }

    @ApiOperation("管理员对开票申请的 XXX")
    @RequestMapping(value = "/{uid}/receipt/{rid}/{action}", method = RequestMethod.POST)
    public @ResponseBody Object receiptRequest(@PathVariable String uid, @PathVariable Integer rid, @PathVariable String action) {
        return receiptService.receiptRequest(uid, rid, action);
    }


	
}
