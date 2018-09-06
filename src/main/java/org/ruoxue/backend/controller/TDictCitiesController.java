package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITDictCitiesService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
@Controller
@RequestMapping("/api/_")
public class TDictCitiesController {

    @Resource
    private ITDictCitiesService dictCitiesService;

    @ApiOperation("根据省份获取城市列表")
    @RequestMapping(value = "/area/city", method = RequestMethod.GET)
    public @ResponseBody Object getCityByProvince(@RequestParam String province){
        return dictCitiesService.getCityByProvince(province);
    }

	
}
