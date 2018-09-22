package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITPublicChargeService;
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
@RequestMapping("/api/charge/public")
public class TPublicChargeController {

    @Resource
    private ITPublicChargeService publicChargeService;

    @ApiOperation("新增申请")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody Object publChargeAdd(@RequestBody JSONObject jsonObject) {
        return publicChargeService.publChargeAdd(jsonObject);
    }

    @ApiOperation("取消申请【M】")
    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
    public @ResponseBody Object publChargeCancel(@PathVariable Integer id) {
        return publicChargeService.publChargeCancel(id);
    }

    @ApiOperation("获取转账记录")
    @RequestMapping(value = "/{status}", method = RequestMethod.GET)
    public @ResponseBody Object listPublChargeStatus(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Date start, @RequestParam(required = false) Date end, @PathVariable String status, @RequestParam(required = false) Integer count) {
        return publicChargeService.listPublChargeStatus(page, size, status, start, end, count);
    }


}
