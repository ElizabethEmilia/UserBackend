package org.ruoxue.backend.controller;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.service.TUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  用户管理接口
 */
@Controller
@RequestMapping("/user")
public class TUserController {

    @Resource
    private TUserService tUserService;

    @RequestMapping(value = "/getid/{userid}", method = RequestMethod.GET)
    public @ResponseBody Object getUserById(@PathVariable Integer userid){
//      调用service
        return tUserService.getTUserById(userid);
    }

}
