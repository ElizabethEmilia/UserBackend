package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-09-11
 */
@Controller
@RequestMapping("/api/role")
public class TRoleController {

    @Resource
    private ITRoleService roleService;

    @ApiOperation("角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody Object list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return roleService.list(page, size);
    }

    @ApiOperation("新增角色")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody Object roleAdd(@RequestBody JSONObject jsonObject) {
        return roleService.roleAdd(jsonObject);
    }

    @ApiOperation("查看角色详情")
    @RequestMapping(value = "/{roleid}", method = RequestMethod.GET)
    public @ResponseBody Object getRole(@PathVariable Integer roleid) {
        return roleService.getRole(roleid);
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    public @ResponseBody Object removeRole(@PathVariable Integer roleid) {
        return roleService.removeRole(roleid);
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "/{roleid}", method = RequestMethod.POST)
    public @ResponseBody Object updateRole(@RequestBody JSONObject jsonObject, @PathVariable Integer roleid) {
        return roleService.updateRole(jsonObject, roleid);
    }
	
}
