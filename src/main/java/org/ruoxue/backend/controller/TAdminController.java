package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.service.ITAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理员控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api/admin")
public class TAdminController extends BaseController {

    /**
     *  修改和查看自己的资料
     */
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


    /**
     *  修改管理员资料
     * @return
     */
    @ApiOperation("获取管理员列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody Object list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return adminService.list(page, size);
    }

    @ApiOperation("新增管理员")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Object adminAdd(@RequestBody JSONObject jsonObject){
        return adminService.adminAdd(jsonObject);
    }

    @ApiOperation("删除管理员")
    @RequestMapping(value = "/{aid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object adminRemove(@PathVariable Integer aid){
        return adminService.removeAdmin(aid);
    }

    @ApiOperation("查看管理员信息")
    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public @ResponseBody Object getAdminByAid(@PathVariable Integer aid){
        return adminService.getAdminByAid(aid);
    }

    @ApiOperation("修改管理员信息")
    @RequestMapping(value = "/{aid}", method = RequestMethod.POST)
    public @ResponseBody Object updateAdmin(@PathVariable Integer aid, @RequestParam String name, @RequestParam Integer roleid){
        return adminService.updateAdmin(aid, name, roleid);
    }




	
}
