package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITComSetProgressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class TComSetProgressController {

    @Resource
    private ITComSetProgressService comSetProgressService;

    @ApiOperation("查看客户公司设立进度")
    @RequestMapping(value = "/{uid}/company/{cid}/setup", method = RequestMethod.GET)
    public @ResponseBody Object getSetUp(@PathVariable String uid, @PathVariable Integer cid){
        return comSetProgressService.getSetUp(uid, cid);
    }

    @ApiOperation("添加客户公司设立进度")
    @RequestMapping(value = "/{uid}/company/{cid}/setup/new", method = RequestMethod.POST)
    public @ResponseBody Object addSetUp(@PathVariable String uid, @PathVariable Integer cid, @RequestParam String status, @RequestParam String note){
        return comSetProgressService.addSetUp(uid, cid, status, note);
    }

    @ApiOperation("删除客户公司设立进度")
    @RequestMapping(value = "/{uid}/company/{cid}/setup/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteSetUp(@PathVariable String uid, @PathVariable Integer cid){
        return comSetProgressService.deleteSetUp(uid, cid);
    }

	
}
