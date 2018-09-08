package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.service.ITCompanyService;
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
public class TCompanyController {

    @Resource
    private ITCompanyService companyService;

    @ApiOperation("客户的公司列表")
    @RequestMapping(value = "/{uid}/company/list", method = RequestMethod.GET)
    public @ResponseBody Object listCompany(@PathVariable String uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return companyService.listCompany(uid, page, size);
    }

    @ApiOperation("客户的某个公司信息")
    @RequestMapping(value = "/{uid}/company/{cid}", method = RequestMethod.GET)
    public @ResponseBody Object getCompany(@PathVariable String uid, @PathVariable Integer cid){
        return companyService.getCompany(uid, cid);
    }

    @ApiOperation("修改客户的某个公司信息")
    @RequestMapping(value = "/{uid}/company/{cid}", method = RequestMethod.POST)
    public @ResponseBody Object updateCompany(@Valid TCompany company, @PathVariable String uid, @PathVariable Integer cid){
        return companyService.updateCompany(company, uid, cid);
    }

    @ApiOperation("删除客户的某个公司")
    @RequestMapping(value = "/{uid}/company/{cid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCompany(@PathVariable String uid, @PathVariable Integer cid){
        return companyService.deleteCompany(uid, cid);
    }

    @ApiOperation("删除客户的所有公司")
    @RequestMapping(value = "/{uid}/company/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCompanys(@PathVariable String uid){
        return companyService.deleteCompanys(uid);
    }

    @ApiOperation("为客户新增公司")
    @RequestMapping(value = "/{uid}/company/new", method = RequestMethod.POST)
    public @ResponseBody Object addCompony(@Valid TCompany company, @PathVariable String uid){
        return companyService.addCompony(company, uid);
    }


	
}
