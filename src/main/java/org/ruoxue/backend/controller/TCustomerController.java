package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  用户管理控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api")
public class TCustomerController extends BaseController {

    @Resource
    private ITCustomerService customerService;

    @ApiOperation("获取账号信息")
    @RequestMapping(value = "/account/basic", method = RequestMethod.GET)
    public @ResponseBody Object basicGet(){
//        获取用户id
        Integer uid = (Integer) getSession().getAttribute("uid");
        return customerService.basicGet(uid);
    }

    @ApiOperation("修改账号信息")
    @RequestMapping(value = "/account/basic", method = RequestMethod.POST)
    public @ResponseBody Object basicPost(@RequestBody String json){
        return customerService.basicPost(JSONObject.parseObject(json, TCustomer.class));
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/account/password", method = RequestMethod.POST)
    public @ResponseBody Object password(@RequestParam String old_pwd, @RequestParam String new_pwd){
        return customerService.password(old_pwd, new_pwd);
    }

    @ApiOperation("修改头像")
    @RequestMapping(value = "/account/avatar", method = RequestMethod.POST)
    public @ResponseBody Object avatar(@RequestParam String img){
        return customerService.avatar(img);
    }

    /**
     *  9月17日客户最新要求
     */
    @ApiOperation("手动延长某个公司的服务期限")
    @RequestMapping(value = "/customer/{uid}/company/{cid}/addtime", method = RequestMethod.POST)
    public @ResponseBody Object adminAddtime(@PathVariable String uid, @PathVariable Integer cid, @RequestParam Integer months, @RequestParam Double price){
        return customerService.adminAddtime(uid, cid, months, price);
    }

    @ApiOperation("查看客户的充值记录（exchange表）")
    @RequestMapping(value = "/customer/{uid}/charge-list", method = RequestMethod.GET)
    public @ResponseBody Object listCharge(@PathVariable Integer uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return customerService.listCharge(uid, page, size);
    }

    @ApiOperation("获取客户即将到期的公司的信息")
    @RequestMapping(value = "/company/deadline", method = RequestMethod.GET)
    public @ResponseBody Object listDeadline(){
        if (getSession().getAttribute("obj") instanceof TAdmin)
            return customerService.listDeadlineAdmin();
        return customerService.listDeadline();
    }

    @ApiOperation("查看自己的充值记录（exchange表）")
    @RequestMapping(value = "/charge-list", method = RequestMethod.GET)
    public @ResponseBody Object listExchangeByUid(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return customerService.listExchangeByUid(page, size);
    }

}
