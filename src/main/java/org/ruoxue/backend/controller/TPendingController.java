package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.service.ITPendingService;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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

    @Resource
    private ITPendingService pendingService;

    @ApiOperation("获取通知(根据权限)")
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public @ResponseBody Object getNotification() {
        /// 用户获取通知(uid)
        if (XunBinKit.getSession().getAttribute("obj") instanceof TCustomer) {
            return pendingService.getNotificationByUid();
        }

        /// 管理员获取通知
        // 没有通知权限  直接拒绝
        if (XunBinKit.shouldReject(PermissionManager.Moudles.ReceiveNotification))
            return null;

        // 管理员可以获取全部用户的通知()
        if (!XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListAll)) {
            return pendingService.getNotification();
        }

        // 管理员仅能获取本组用户的通知(gid)
        if (!XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListOfCurrentGroup)) {
            return pendingService.getNotificationByGid();
        }

        // 管理只能获取自己的所属用户的通知(aid)
        return pendingService.getNotificationByAid();
    }

    @ApiOperation("处理通知")
    @RequestMapping(value = "/notofication/{nid}", method = RequestMethod.POST)
    public @ResponseBody Object removeNoto(@PathVariable Integer nid) {
        return pendingService.removeNoto(nid);
    }


}
