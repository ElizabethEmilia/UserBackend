package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITConfigService;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/api/system")
public class TConfigController {

    @Resource
    private ITConfigService configService;

    @ApiOperation("系统设置列表")
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public @ResponseBody Object setting(){
        if (XunBinKit.shouldReject(PermissionManager.Moudles.SystemConfigAdvanced)) return null;
        return configService.setting();
    }

    @ApiOperation("获取系统设置, 根据key取value值")
    @RequestMapping(value = "/settings/{key}", method = RequestMethod.GET)
    public @ResponseBody Object settingKey(@PathVariable String key){
        return configService.settingKey(key);
    }

    @ApiOperation("修改系统设置")
    @RequestMapping(value = "/settings/{key}", method = RequestMethod.POST)
    public @ResponseBody Object updateSettingKey(@PathVariable String key, @RequestParam String value){
        return configService.updateSettingKey(key, value);
    }

    @ApiOperation("清空回收站, 删除表中状态为3的 数据(客户表、管理员表)")
    @RequestMapping(value = "/settings/clear-cache", method = RequestMethod.GET)
    public @ResponseBody Object clearCache(){
        return configService.clearCache();
    }


}
