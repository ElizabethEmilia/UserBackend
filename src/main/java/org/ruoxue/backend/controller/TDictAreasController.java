package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITDictAreasService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class TDictAreasController {

    @Resource
    private ITDictAreasService dictAreasService;

    @ApiOperation("根据市份获取区表")
    @RequestMapping(value = "/area/district", method = RequestMethod.GET)
    public @ResponseBody Object getDistrictByCity(@RequestParam String city){
        return dictAreasService.getDistrictByCityId(city);
    }
	
}
