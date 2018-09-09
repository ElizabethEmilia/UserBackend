package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.ITAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 管理员控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api/admin")
public class TAdminController extends BaseController {

    @Resource(name = "TAdminServiceImpl")
    private ITAdminService adminService;

    @ApiOperation("获取账号信息")
    @RequestMapping(value = "/basic", method = RequestMethod.GET)
    public @ResponseBody Object basicGet(){
//        获取用户id
        Integer uid = (Integer) getSession().getAttribute("uid");
        return adminService.basicGet(uid);
    }

    @ApiOperation("修改账号信息")
    @RequestMapping(value = "/basic", method = RequestMethod.POST)
    public @ResponseBody Object basicPost(@RequestBody JSONObject jsonObject){
        return adminService.basicPost(jsonObject);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public @ResponseBody Object password(@RequestParam String old_pwd, @RequestParam String new_pwd){
        return adminService.password(old_pwd, new_pwd);
    }



	
}
