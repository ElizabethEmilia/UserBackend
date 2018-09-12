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

export default [
    { path: '/', component: window.config && window.config.isAdmin ? Admin : Account },
    { path: '/account', component: Account },
    { path: '/company', component: Company },
    { path: '/customer', component: Customer },
    { path: '/log', component: Log },
    { path: '/receipt', component: Receipt },
    { path: '/tax', component: Tax },
    { path: '/user', component: User },
    { path: '/system', component: System },
    { path: '/admin', component: Admin },
    { path: '/orders', component: Orders },
    { path: '/role', component: Role },
    { path: "/group", component: Group },
]