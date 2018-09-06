package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.service.ITSigninService;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.Valid;

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
public class TSigninController {

    @Resource
    private ITSigninService signinService;

    @ApiOperation("获取用户基本信息")
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public @ResponseBody Object getCustomerByUid(@RequestParam Integer uid) {
        return signinService.getCustomerByUid(uid);
    }

    @ApiOperation("修改用户基本信息")
    @RequestMapping(value = "/{uid}", method = RequestMethod.POST)
    public @ResponseBody Object updateCustomer(@Valid TCustomer customer, @PathVariable Integer uid) {
        return signinService.updateCustomer(customer, uid);
    }

    @ApiOperation("删除一个用户U")
    @RequestMapping(value = "/{uid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCustomer(@PathVariable Integer uid) {
        return signinService.deleteCustomer(uid);
    }

    @ApiOperation("获取用户信息列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody Object listCustomer(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return signinService.listCustomer(page, size);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/{uid}/password", method = RequestMethod.POST)
    public @ResponseBody Object updatePwssword(@RequestParam String password, @PathVariable Integer uid) {
        return signinService.updatePwssword(password, uid);
    }
	
}
