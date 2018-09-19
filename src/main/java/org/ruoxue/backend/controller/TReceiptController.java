package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
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
@RequestMapping("/api")
public class TReceiptController {

    @Resource
    private ITReceiptService receiptService;

    @ApiOperation("查看客户公司的开票申请")
    @RequestMapping(value = "/customer/{uid}/receipt", method = RequestMethod.GET)
    public @ResponseBody Object listReceipt(@PathVariable String uid, @RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                            @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer type,
                                            @RequestParam(required = false) Date start, @RequestParam(required = false) Date end){
        return receiptService.listReceipt(uid, cid, page, size, type, status, start, end);
    }

    @ApiOperation("管理员对开票申请的 XXX")
    @RequestMapping(value = "/customer/{uid}/receipt/{rid}/{action}", method = RequestMethod.POST)
    public @ResponseBody Object receiptRequest(@PathVariable String uid, @PathVariable Integer rid, @PathVariable String action, @RequestParam(required = false) String reason) {
        return receiptService.receiptRequest(uid, rid, action, reason);
    }


    /**
     *  开票管理模块
     */
    @ApiOperation("申请开票")
    @RequestMapping(value = "/receipt/new", method = RequestMethod.POST)
    public @ResponseBody Object receiptAdd(@RequestBody JSONObject jsonObject) {
        return receiptService.receiptAdd(jsonObject);
    }

    @ApiOperation("开票列表")
    @RequestMapping(value = "/receipt/list", method = RequestMethod.GET)
    public @ResponseBody Object receiptList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer cid,
                                            @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer status, @RequestParam(required = false) Date start,
                                            @RequestParam(required = false) Date end) {
        return receiptService.receiptList(page, size, cid, type, status, start, end);
    }

    @ApiOperation("删除开票申请")
    @RequestMapping(value = "/receipt/{rid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object removeReceipt(@PathVariable Integer rid) {
        return receiptService.removeReceipt(rid);
    }

    @ApiOperation("获取全部记录")
    @RequestMapping(value = "/receipt/export", method = RequestMethod.GET)
    public @ResponseBody Object exportReceipt() {
        return receiptService.exportReceipt();
    }

    @ApiOperation("开票情况统计,开票情况统计")
    @RequestMapping(value = "/receipt/stat", method = RequestMethod.GET)
    public @ResponseBody Object statReceipt(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return receiptService.statReceipt(page, size);
    }

    @ApiOperation("将开票状态设置为提交")
    @RequestMapping(value = "/receipt/{rid}/submit", method = RequestMethod.POST)
    public @ResponseBody Object updateStatusToSub(@PathVariable Integer rid) {
        return receiptService.updateStatusToSub(rid);
    }



}
