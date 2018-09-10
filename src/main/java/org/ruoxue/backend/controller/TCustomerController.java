package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.Base64Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *  用户管理控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api/account")
public class TCustomerController extends BaseController {

    @Resource
    private ITCustomerService customerService;

    @ApiOperation("获取账号信息")
    @RequestMapping(value = "/basic", method = RequestMethod.GET)
    public @ResponseBody Object basicGet(){
//        获取用户id
        Integer uid = (Integer) getSession().getAttribute("uid");
        return customerService.basicGet(uid);
    }

    @ApiOperation("修改账号信息")
    @RequestMapping(value = "/basic", method = RequestMethod.POST)
    public @ResponseBody Object basicPost(@RequestBody String json){
        return customerService.basicPost(JSONObject.parseObject(json, TCustomer.class));
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public @ResponseBody Object password(@RequestParam String old_pwd, @RequestParam String new_pwd){
        return customerService.password(old_pwd, new_pwd);
    }

    @ApiOperation("修改头像")
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public @ResponseBody Object avatar(@RequestParam String img){
        // Initialize image file saving path
        if (Base64Util.baseImageSavePath == null) {
            String savePath=System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            Base64Util.baseImageSavePath = savePath;
        }
        return customerService.avatar(img);
    }



}
