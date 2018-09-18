import Account from './routers/account.vue';
import Company from './routers/company.vue';
import Customer from './routers/customer.vue';
import Log from './routers/log.vue';
import Receipt from "./routers/receipt.vue";
import Tax from './routers/tax.vue';
import User from './routers/user.vue';
import System from './routers/system.vue';
import Admin from './routers/admin.vue';
import Orders from './routers/orders.vue';
import Role from './routers/role.vue';
import Group from './routers/group.vue';
import ServicePack from './routers/srvpack.vue';
import PM from '../js/permission.js';

export default (function() {

    let userModules = [
        { path: '/', component: window.config && window.config.isAdmin ? Admin : Account },
        { path: '/account', component: Account },
        { path: '/company', component: Company },
        { path: '/tax', component: Tax },
        { path: '/receipt', component: Receipt },
        { path: '/orders', component: Orders },
    ];
    let commonModules = [
        { path: "/srvpack", component: ServicePack },
    ];
    // 用户模块
    if (!window.config.isAdmin) {
        return userModules.concat(commonModules);
    }

    // 管理员模块
    let adminModules = [
        { path: '/admin', component: Admin },
        { path: '/', component: Admin },
        { path: '/customer', component: Customer },
        ...commonModules
    ];

    PM.filterArrayElements(PM.Modules.LogViewAndExport, adminModules,
        { path: '/log', component: Log });
    PM.filterArrayElements(PM.Modules.AdminUserListOfCurrentGroup, adminModules,
        { path: '/user', component: User });
    PM.filterArrayElements(PM.Modules.SystemConfigAdvanced, adminModules,
        { path: '/system', component: System });
    PM.filterArrayElements(PM.Modules.PermissionManger, adminModules,
        { path: '/role', component: Role });
    PM.filterArrayElements([PM.Modules.AdminUserListAll, PM.Modules.AdminUserModifyOthers], adminModules,
        { path: "/group", component: Group});
    return adminModules;
})();