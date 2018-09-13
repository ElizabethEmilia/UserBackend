package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITGroupService;
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
@RequestMapping("/api/group")
public class TGroupController {

    @Resource
    private ITGroupService groupService;

    @ApiOperation("组列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody Object list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return groupService.listGroup(page, size);
    }

    @ApiOperation("简化的组列表")
    @RequestMapping(value = "/list-simple", method = RequestMethod.GET)
    public @ResponseBody Object listSimple() {
        return groupService.listSimple();
    }

    @ApiOperation("添加组")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Object groupAdd(@RequestBody JSONObject jsonObject) {
        return groupService.groupAdd(jsonObject);
    }

    @ApiOperation("删除组")
    @RequestMapping(value = "/{gid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object groupRemove(@PathVariable Integer gid) {
        return groupService.groupRemove(gid);
    }

    @ApiOperation("修改组")
    @RequestMapping(value = "/{gid}/modify", method = RequestMethod.POST)
    public @ResponseBody Object groupUpdate(@PathVariable Integer gid, @RequestParam String name, @RequestParam String remark) {
        return groupService.groupUpdate(gid, name, remark);
    }

    @ApiOperation("查看组内的管理员")
    @RequestMapping(value = "/{gid}/user", method = RequestMethod.GET)
    public @ResponseBody Object listGroupAdmin(@PathVariable Integer gid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return groupService.listGroupAdmin(gid, page, size);
    }

    @ApiOperation("查看组内管理员所属的客户")
    @RequestMapping(value = "/{gid}/customer", method = RequestMethod.GET)
    public @ResponseBody Object listGroupConstomer(@PathVariable Integer gid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return groupService.listGroupCustomer(gid, page, size);
    }
	
}
