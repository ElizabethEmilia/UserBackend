package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITTaxAccountService;
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
public class TTaxAccountController {

    @Resource
    private ITTaxAccountService taxAccountService;

    /**
     *  税金账户管理(客户操作)
     */
    @ApiOperation("统计税金账户余额，按年按公司统计")
    @RequestMapping(value = "/tax/stat", method = RequestMethod.GET)
    public @ResponseBody Object listTaxStat(@RequestParam(required = false) Integer cid, @RequestParam(required = false) Date yfrom, @RequestParam(required = false) Date yto, @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        return taxAccountService.listTaxStat(cid, yfrom, yto, page, size);
    }

    @ApiOperation("税金账户明细")
    @RequestMapping(value = "/tax/detail", method = RequestMethod.GET)
    public @ResponseBody Object listTaxDetail(@RequestParam(required = false) Integer cid, @RequestParam(required = false) Date mfrom, @RequestParam(required = false) Date mto, @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        return taxAccountService.listTaxDetail(cid, mfrom, mto, page, size);
    }


    /**
     *  税金账户管理(管理员操作)
     */
    @ApiOperation("统计税金账户余额，按年按公司统计")
    @RequestMapping(value = "/customer/{cid}/tax/stat", method = RequestMethod.GET)
    public @ResponseBody Object listTaxStatAdmin(@PathVariable Integer cid, @RequestParam(required = false) Date yfrom, @RequestParam(required = false) Date yto, @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
        return taxAccountService.listTaxStatAdmin(cid, yfrom, yto, page, size);
    }

    @ApiOperation("税金账户明细")
    @RequestMapping(value = "/customer/{cid}/tax/detail", method = RequestMethod.GET)
    public @ResponseBody Object listTaxDetailAdmin(@PathVariable Integer cid, @RequestParam(required = false) Date mfrom, @RequestParam(required = false) Date mto, @RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size) {
        return taxAccountService.listTaxDetailAdmin(cid, mfrom, mto, page, size);
    }

    @ApiOperation("管理员手动后台操作税金")
    @RequestMapping(value = "/tax/modify", method = RequestMethod.POST)
    public @ResponseBody Object updateTax(@RequestParam Integer uid, @RequestParam Integer cid, @RequestParam Double tax) {
        return taxAccountService.updateTax(cid, uid, tax);
    }

	
}
