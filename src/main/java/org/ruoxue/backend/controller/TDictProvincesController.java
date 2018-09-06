package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITDictProvincesService;
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
public class TDictProvincesController {

    @Resource
    private ITDictProvincesService dictProvincesService;

    @ApiOperation("获取省份列表")
    @RequestMapping(value = "/area/province", method = RequestMethod.GET)
    public @ResponseBody Object getProvince(){
        return dictProvincesService.getProvince();
    }

    @ApiOperation("导出任意JSON对象数组到csv表格")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody  Object jsonToCVS(@RequestParam String json, @RequestParam String filename) {
        return dictProvincesService.jsonToCVS(json, filename);
    }
	
}
