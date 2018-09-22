package org.ruoxue.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.util.XunBinKit;
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
        /// 用户获取通知
        if (XunBinKit.getSession().getAttribute("obj") instanceof TCustomer) {
            /// TODO: 获取发给用户的通知(receiver=0)
            return 0;
        }

        /// 管理员获取通知
        // 没有通知权限  直接拒绝
        if (XunBinKit.shouldReject(PermissionManager.Moudles.ReceiveNotification))
            return null;

        // 管理员可以获取全部用户的通知
        if (!XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListAll)) {
            ///  TODO: 获取全部用户的通知（不作uid/gid任何的筛选）
            return 0;
        }

        // 管理员仅能获取本组用户的通知
        if (!XunBinKit.shouldReject(PermissionManager.Moudles.AdminCustomerListOfCurrentGroup)) {
            /// TODO: 获取本组的用户通知（筛选gid）
            return 0;
        }

        // 管理只能获取自己的所属用户的通知
        /// TODO: 获取自己的用户的通知（筛选aid）

        return 1;
    }

}
