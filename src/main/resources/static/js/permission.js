const Modules = {

    /***  客户相关权限 ***/
    // 用户基本信息模块
    CustomerAccountInfomation: 0,

    // 充值相关模块
    CustomerCharge: 1,

    // 客户界面的其他操作
    CustomerNormalActions: 2,

    /*** 管理员相关权限 ***/
    // 当前用户名下的客户信息查看
    AdminCustomerListOfCurrentAccount: 3,

    // 当前组的所有客户信息查看
    AdminCustomerListOfCurrentGroup: 4,

    // 所有客户信息查看
    AdminCustomerListAll: 5,

    // 客户信息的增加 (在个人或他人名下增加的权限需要组合判断)
    AdminCustomerAdd: 6,

    // 客户信息的修改
    AdminCustomerModify: 7,

    // 客户信息的删除
    AdminCustomerRemoval: 8,

    // 客户公司信息的增改
    AdminCompanyAddAndModify: 9,

    // 客户公司的删除
    AdminCompanyRemoval: 10,

    // 用户管理：

    // 列出当前组下其他用户
    AdminUserListOfCurrentGroup: 11,

    // 列出所有用户
    AdminUserListAll: 12,

    // 修改用户信息
    AdminUserModifyOthers: 13,

    // 删除其他用户
    AdminUserRemoveOthers: 14,

    // 增加用户
    AdminUserAdd: 15,

    /// 日志管理
    // 查看和导出日志
    LogViewAndExport: 16,

    // 删除日志
    LogDelete: 17,

    // 权限管理
    PermissionManger: 18,

    // 普通系统内配置
    SystemConfigNormal: 19,

    // 高级系统
    SystemConfigAdvanced: 20,

    // 为客户设置打折价
    SetDiscount: 21,

    /// 其它权限
    // 审核公司
    CheckCompany: 22,

    //为客户充值
    ChargeForCustomer: 23,

    // 开票
    ReceiptForCustomer:24,

    // 获取通知
    ReceiveNotification: 25,

    // 标记通知状态
    ProcessNotification: 26,

    // 新增订单
    AddOrderForCustomer: 27,
};

function canAccess(moudleID, userPermission) {
    return (userPermission & (1 << moudleID)) > 0;
}

function canAccessWithPermission(modulePermssion, userPermission) {
    return (userPermission & modulePermssion) > 0;
}

function getPermission() {
    return window.config && window.config.permission ?
        window.config.permission : 0;
}

function canAccessWithModule(moudleID) {
    return canAccess(moudleID, getPermission());
}

function shouldReject(moudleID) {
    return !canAccessWithModule(moudleID);
}

function filterObject(moduleID, obj) {
    return shouldReject(moduleID) ? {} : obj;
}

function filterArrayElements(moduleID, arr, obj) {
    if (moduleID instanceof Array)
        return !(moduleID.filter(shouldReject).length || !arr.push(obj));
    return !(shouldReject(moduleID) || !arr.push(obj));
}

function forAllPermissions() {
    let keys = Object.keys(Modules);
    let perm = keys.map(e=>({[e]: !shouldReject(Modules[e])}));
    let r = Object.assign({}, ...perm);
    return r;
}

export default {
    canAccess,
    canAccessWithPermission,
    getPermission,
    canAccessWithModule,
    filterObject,
    shouldReject,
    filterArrayElements,
    forAllPermissions,
    Modules,
}