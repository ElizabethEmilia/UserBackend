package org.ruoxue.backend.controller;

import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITAreaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-09-08
 */
@Controller
@RequestMapping("/api/_/area")
public class TAreaController {

    @Resource
    private ITAreaService areaService;

    @ApiOperation("获取省份列表")
	@RequestMapping(value="/province", method = RequestMethod.GET)
    public @ResponseBody Object getProvince() {
        return areaService.getProvince();
    }

    @ApiOperation("获取城市")
    @RequestMapping(value="/city", method = RequestMethod.GET)
    public @ResponseBody Object getCity(String province) {
        return areaService.getCity(province);
    }

    @ApiOperation("获取地区")
    @GetMapping("/district")
    public @ResponseBody Object getDistrict(String province, String city) {
        return areaService.getDistrict(province, city);
    }
}
