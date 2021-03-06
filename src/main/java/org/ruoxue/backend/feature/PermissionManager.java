package org.ruoxue.backend.feature;

/*
    权限管理器：在此模块配置各个模块的权限，并根据该模块的定义获取权限
 */
public class PermissionManager {
    public class Moudles {

        /***  客户相关权限 ***/
        // 用户基本信息模块
        public static final int CustomerAccountInfomation = 0;

        // 充值相关模块
        public static final int CustomerCharge = 1;

        // 客户界面的其他操作
        public static final int CustomerNormalActions = 2;

        /*** 管理员相关权限 ***/
        // 当前用户名下的客户信息查看
        public static final int AdminCustomerListOfCurrentAccount = 3;

        // 当前组的所有客户信息查看
        public static final int AdminCustomerListOfCurrentGroup = 4;

        // 所有客户信息查看
        public static final int AdminCustomerListAll = 5;

        // 客户信息的增加 (在个人或他人名下增加的权限需要组合判断)
        public static final int AdminCustomerAdd = 6;

        // 客户信息的修改
        public static final int AdminCustomerModify = 7;

        // 客户信息的删除
        public static final int AdminCustomerRemoval = 8;

        // 客户公司信息的增改
        public static final int AdminCompanyAddAndModify = 9;

        // 客户公司的删除
        public static final int AdminCompanyRemoval = 10;

        // 用户管理：


        // 列出当前组下其他用户
        public static final int AdminUserListOfCurrentGroup = 11;

        // 列出所有用户
        public static final int AdminUserListAll = 12;

        // 修改用户信息
        public static final int AdminUserModifyOthers = 13;

        // 删除其他用户
        public static final int AdminUserRemoveOthers = 14;

        // 增加用户
        public static final int AdminUserAdd = 15;

        /// 日志管理
        // 查看和导出日志
        public static final int LogViewAndExport = 16;

        // 删除日志
        public static final int LogDelete = 17;

        // 权限管理
        public static final int PermissionManger = 18;

        // 普通系统内配置
        public static final int SystemConfigNormal = 19;

        // 高级系统
        public static final int SystemConfigAdvanced = 20;

        // 为客户设置打折价
        public static final int SetDiscount = 21;

        /// 其它权限

        // 审核公司
        public static final int CheckCompany = 22;

        // 为客户充值
        public static final int ChargeForCustomer = 23;

        // 开票
        public static final int ReceiptForCustomer = 24;

        // 获取通知
        public static final int ReceiveNotification = 25;

        // 标记通知状态
        public static final int ProcessNotification = 26;

        // 为客户新增订单
        public static final int AddOrderForCustomer = 27;

        // 为客户修改年销售额范围
        public static final int ModifyExpectedIncomeForCustomer = 28;

        //可以给更高的档位
        public static final int CanGiveHigherRangeOfExpectedIncome = 29;

    }

    public static boolean canAccess(int moudleID, int userPermission) {
        return (userPermission & (1 << moudleID)) > 0;
    }

    public static boolean canAccessWithPermission(int modulePermssion, int userPermission) {
        return (userPermission & modulePermssion) > 0;
    }

}
