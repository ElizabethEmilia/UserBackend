package org.ruoxue.backend.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITComCertService;
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
public class TComCertController {

    @Resource
    private ITComCertService comCertService;

    @ApiOperation("查看客户公司的证件列表")
    @RequestMapping(value = "/{uid}/company/{cid}/cert", method = RequestMethod.GET)
    public @ResponseBody Object listCert(@PathVariable String uid, @PathVariable Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return comCertService.listCert(uid, cid, page, size);
    }

    @ApiOperation("查看客户公司的某个证件")
    @RequestMapping(value = "/{uid}/cert/{certid}", method = RequestMethod.GET)
    public @ResponseBody Object getCert(@PathVariable String uid, @PathVariable Integer certid){
        return comCertService.getCert(uid, certid);
    }

    @ApiOperation("删除客户公司的某个证件")
    @RequestMapping(value = "/{uid}/cert/{certid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCert(@PathVariable String uid, @PathVariable Integer certid){
        return comCertService.deleteCert(uid, certid);
    }

    @ApiOperation("上传客户公司的证件")
    @RequestMapping(value = "/{uid}/company/{cid}/newcert", method = RequestMethod.POST)
    public @ResponseBody Object addCert(@RequestBody JSONObject jsonObject, @PathVariable String uid, @PathVariable Integer cid){
        return comCertService.addCert(jsonObject, uid, cid);
    }


	
}
