package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengjb
 * @since 2018-09-22
 */
@Controller
@RequestMapping("/api")
public class TPendingController {

    @ApiOperation("获取通知(根据权限)")
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public @ResponseBody Object getNotification() {
        return 1;
    }

}
