package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITPublicChargeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class TPublicChargeController {

    @Resource
    private ITPublicChargeService publicChargeService;

    @ApiOperation("修改客户的对公充值状态(确认/取消)")
    @RequestMapping(value = "/{uid}/publiccharge/{pid}/{status}", method = RequestMethod.POST)
    public @ResponseBody Object updatePublicchargeStatus(@PathVariable Integer uid, @PathVariable Integer pid, @PathVariable String status) {
        return publicChargeService.updatePublicchargeStatus(uid, pid, status);
    }
	
}
