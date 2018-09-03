package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITCustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *  用户管理控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api")
public class TCustomerController {

    @Resource
    private ITCustomerService customerService;

    @ApiOperation("用户注册接口,同步插入sign表")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody Object CustomerRegister(@RequestBody JSONObject jsonObject){
        return customerService.CustomerRegister(jsonObject);
    }

}
