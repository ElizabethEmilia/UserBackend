package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITCompanyService;
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
@RequestMapping("/api")
public class TCompanyController {

    @Resource
    private ITCompanyService companyService;

    @ApiOperation("客户的公司列表")
    @RequestMapping(value = "/customer/{uid}/company/list", method = RequestMethod.GET)
    public @ResponseBody Object listCompany(@PathVariable String uid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return companyService.listCompany(uid, page, size);
    }

    @ApiOperation("客户的某个公司信息")
    @RequestMapping(value = "/customer/{uid}/company/{cid}", method = RequestMethod.GET)
    public @ResponseBody Object getCompany(@PathVariable String uid, @PathVariable Integer cid){
        return companyService.getCompany(uid, cid);
    }

    @ApiOperation("修改客户的某个公司信息")
    @RequestMapping(value = "/customer/{uid}/company/{cid}", method = RequestMethod.POST)
    public @ResponseBody Object updateCompany(@Valid TCompany company, @PathVariable String uid, @PathVariable Integer cid){
        return companyService.updateCompany(company, uid, cid);
    }

    @ApiOperation("删除客户的某个公司")
    @RequestMapping(value = "/customer/{uid}/company/{cid}/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCompany(@PathVariable String uid, @PathVariable Integer cid){
        return companyService.deleteCompany(uid, cid);
    }

    @ApiOperation("删除客户的所有公司")
    @RequestMapping(value = "/customer/{uid}/company/delete", method = RequestMethod.POST)
    public @ResponseBody Object deleteCompanys(@PathVariable String uid){
        return companyService.deleteCompanys(uid);
    }

    @ApiOperation("为客户新增公司")
    @RequestMapping(value = "/customer/{uid}/company/new", method = RequestMethod.POST)
    public @ResponseBody Object addCompony(@Valid TCompany company, @PathVariable String uid){
        return companyService.addCompony(company, uid);
    }

    @ApiOperation("删除设立进度")
    @PostMapping("/customer/_/company/{cid}/setup/{setupid}/delete")
    public @ResponseBody Object removeSetupState(@PathVariable Integer cid,@PathVariable Integer setupid) {
        return companyService.removeSetupState(cid, setupid);
    }

    /**
     *  公司管理模块
     */
    @ApiOperation("获取公司列表")
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public @ResponseBody Object listCompanys(@RequestParam(required = false) String search, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return companyService.listCompanys(search, page, size);
    }

    @ApiOperation("获取某一个公司的信息")
    @RequestMapping(value = "/company/{cid}/info", method = RequestMethod.GET)
    public @ResponseBody Object getCompanyInfo(@PathVariable Integer cid) {
        if (XunBinKit.shouldReject(PermissionManager.Moudles.AdminCompanyAddAndModify)) return null;
        return companyService.getCompanyInfo(cid);
    }

    @ApiOperation("获取某一个公司设立进度，并按照时间排序")
    @RequestMapping(value = "/company/{cid}/setup", method = RequestMethod.GET)
    public @ResponseBody Object getCompanySetUp(@PathVariable Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return companyService.getCompanySetUp(cid, page, size);
    }

    @ApiOperation("获取某一个公司证件照")
    @RequestMapping(value = "/company/{cid}/cert", method = RequestMethod.GET)
    public @ResponseBody Object getCompanyCert(@PathVariable Integer cid, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return companyService.getCompanyCert(cid, page, size);
    }

    @ApiOperation("获取某一个公司的证件照信息")
    @RequestMapping(value = "/company/{cid}/cert/{id}", method = RequestMethod.GET)
    public @ResponseBody Object countCompanyCertById(@PathVariable Integer cid, @PathVariable Integer id) {
        return companyService.countCompanyCertById(cid, id);
    }

    @ApiOperation("获取该用户拥有的公司数量")
    @RequestMapping(value = "/company/count", method = RequestMethod.GET)
    public @ResponseBody Object countCompany() {
        return companyService.countCompany();
    }


}
