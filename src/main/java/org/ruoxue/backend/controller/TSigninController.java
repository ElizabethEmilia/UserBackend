package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITSigninService;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("新增客户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Object customerAdd(@RequestBody JSONObject jsonObject) {
        return signinService.customerAdd(jsonObject);
    }

    @ApiOperation("获取用户基本信息")
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public @ResponseBody Object getCustomerByUid(@PathVariable Integer uid) {
        return signinService.getCustomerByUid(uid);
    }

    @ApiOperation("修改用户基本信息")
    @RequestMapping(value = "/{uid}", method = RequestMethod.POST)
    public @ResponseBody Object updateCustomer(@Valid TCustomer customer, @PathVariable Integer uid) {
        return signinService.updateCustomer(customer, uid);
    }

    @ApiOperation("删除一个用户")
    @RequestMapping(value = "/{uid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCustomer(@PathVariable Integer uid) {
        return signinService.deleteCustomer(uid);
    }

    @ApiOperation("获取用户信息列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody Object listCustomer(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer count) {
        if (XunBinKit.shouldReject(PermissionManager.Moudles.AdminUserListAll)) return null;
        return signinService.listCustomer(page, size, count);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/{uid}/password", method = RequestMethod.POST)
    public @ResponseBody Object updatePwssword(@RequestParam String password, @PathVariable Integer uid) {
        return signinService.updatePwssword(password, uid);
    }


    /**
     *  获取客户列表
     * ** 可以根据客户名称和id搜索
     */
    @ApiOperation("获取客户列表(self, group, all)")
    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public @ResponseBody Object listByType(@PathVariable String type, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String search, @RequestParam(required = false) Integer count) {
        if (type.equals("group") && XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListOfCurrentGroup))
            return null;
        if (type.equals("all") && XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListAll))
            return null;
        return signinService.listByType(type, page, size, search, count);
    }


}
