package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.service.ITLogsService;
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
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api")
public class TLogsController {

    @Resource
    private ITLogsService logsService;

    @ApiOperation("日志列表")
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public @ResponseBody Object list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return logsService.listLog(page, size);
    }

	
}
