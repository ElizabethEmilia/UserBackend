import $ from './ajax';
import util from './util.js'

const __rd_post_body = { r: Math.random() };

async function __Miyuki_RequestGET(url) {
    try {
        let r = await $.ajax(url);
        if (r.code  === 0) {
            return r.data;
        }
        throw new Error(r.msg);
    }
    catch(err) {
        throw new Error("和服务器之间的通讯发现错误");
    }
}

async function __Miyuki_RequestPOST(url, data) {
    try {
        let r = await $.ajax(url, data);
        if (r.code  === 0) {
            return r.data;
        }
        throw new Error(r.msg);
    }
    catch(err) {
        throw new Error("和服务器之间的通讯发现错误");
    }
}

async function __Miyuki_RequestDELETE(url, data) {
    try {
        let r = await $.DELETE(url);
        if (r.code  === 0) {
            return r.data;
        }
        throw new Error(r.msg);
    }
    catch(err) {
        throw new Error("和服务器之间的通讯发现错误");
    }
}

function GET(url) {
    return function() {
        return __Miyuki_RequestGET(url);
    }
}

function POST(url, data) {
    return function() {
        return __Miyuki_RequestPOST(url, data);
    }
}

function DELETE(url) {
    return function() {
        return __Miyuki_RequestDELETE(url);
    }
}

function Settings_Update(name, value) {
    return (POST(`/api/settings/${ name }`, { value }))();
}

function Settings_Get(name) {
    return (GET(`/api/settings/${ name }`))();
}

export default {
    Login: {
        heartbeat: GET('/api/login/heartbeat'),
    },
    Customer: {
        deleteUser: (uid) => POST(`/api/customer/${uid}/delete`, __rd_post_body)(),
        addNew: (data) => POST(`/api/customer/add`, data)(),

        Company: {
            getList: (uid) => POST(`/api/customer/${ uid }/company/list`)(),
            newCompany: (uid, data) => POST(`/api/customer/${uid}/company/new`, data)(),
            deleteCompany: (cid, uid='_') => POST(`/api/customer/${uid}/company/${cid}/delete`, __rd_post_body)(),
            renew: (uid, cid, params) => POST(`/api/customer/${uid}/company/${cid}/addtime`, params)(),
        },
    },
    Account: {
        getBasicInfo: GET("/api/account/basic"),
    },

    Company: {
        getCount: GET("/api/company/count"),
        getList: GET("/api/company/list?size=10000"),

        SetupStates: {
            update: (stateArray) => Settings_Update('setup_states', stateArray.join(',')),
        },
        Certificates: {
            remove: (cid) => POST(`/api/customer/_/cert/${cid}/delete`, { r: 1})(),
            add: (cid, data) => POST(`/api/customer/_/company/${cid}/newcert`, data)(),
        },
        SetupProgress: {
            add: (cid, data) => POST(`/api/customer/_/company/${cid}/setup/new`, data)(),
            remove: (cid, sid) => POST(`/api/customer/_/company/${cid}/setup/${sid}/delete`, {r:1})(),
        }
    },
    Setting: {
        get: Settings_Get,
        set: Settings_Update,
        list: GET("/api/setting/list"),
    },

    // h获取地区
    Area: {
        getProvince: GET("/api/_/area/province"),
        getCity: (province) => GET("/api/_/area/city?"+util.forGetParamsN({ province }))(),
        getDistruct: (province, city) => GET("/api/_/area/district?"+util.forGetParamsN({ province, city }))(),

    },

    // 角色
    Role: {
        getList: GET("/api/role/list?size=10000"),
        remove: roleid => DELETE("/api/role/" + roleid)(),
        get: roleid => GET("/api/role/" + roleid)(),
        modify: (roleid, data) => POST(`/api/role/${roleid}`, data)(),
        addNew: (data) => POST('/api/role/new', data)(),
    },

    // 税金预交
    Tax: {

        PreSelect: {
            getLastItem: (cid) => GET(`/api/company/${cid}/sales/last`)(),
            List: {
                current: (cid) => GET(`/api/company/${cid}/current`)(),
                all: (cid) => GET(`GET /api/company/${cid}/all`)(),
            },
            Administration: {
                List: {
                    current: cid=>GET(`/api/customer/_/company/${cid}/sales/list/current`)(),
                    all: cid=>GET(`/api/customer/{uid|_}/company/${cid}/sales/list/all`)(),
                },
                Actions: {
                    requestBack: cid=>POST(`/api/customer/_/company/${cid}/sales/request-back`)(),
                    preSelect: cid=>POST(`/api/customer/_/company/${cid}/sales/preselect`)(),
                    reselect: cid=>POST(`/api/customer/_/company/${cid}/sales/reselect`)(),
                },
            },
            preSelect: (cid, range) => POST(`/api/company/${cid}/sales/preselect`, { ysaRange: range })(),
            reselect: (cid, range) => POST(`/api/company/${cid}/sales/reselect`, { ysaRange: range })(),
            complement: (cid) => POST(`/api/company/${cid}/sales/complement`, { r: 1 })(),
            withdraw: (cid) => POST(`/api/company/${cid}/sales/withdraw`, { r: 1 })(),
        },

        Account: {

        }
    },

    // 组
    Group: {
        getList: GET(`/api/group/list`),
        getSimplifiedList: GET(`/api/group/list-simple`),
        add: data => POST(`/api/group/add`, data)(),
        remove: gid => POST(`/api/group/${gid}/delete`, { r: 1 })(),
        modify: (gid, data) => POST(`/api/group/${gid}/modify`, data)(),
        getUserOfGroup: (gid) => GET(`/api/group/${gid}/user?size=10000`)(),
        getCustomerOfGroup: (gid) => GET(`/api/group/${gid}/customer`)(),
    },

    Admin: {
        add: data => POST(`/api/admin/add`, data)(),
        getList: GET(`/api/admin/list?size=0`),
        remove: aid => POST(`/api/admin/${aid}/delete`, {r:1})(),
        get: aid => GET(`/api/admin/${aid}/`)(),
        modify: (aid, data) => POST(`/api/admin/${aid}/`, data)(),
    },

    Verification: {
        getImages: GET("/api/verifycode2"),
        verify: x => POST("/api/verifycode2", { x })(),
    },

    ShopItems: {
        getList: GET("/api/items/list"),
        add:  data => POST("/api/items/add", data)(),
        modify: (itemid, data) => POST(`/api/items/{itemid}/modify`)(),
        get: itemid => GET(`/api/items/${itemid}`)(),
    },

    
    Payment: {
        Alipay: {
            query: (running) => GET("/api/pay/alipay/query?" + util.forGetParams({running}))(),
            start: (params) => GET("/api/pay/alipay/start?" + util.forGetParams(params))(),
        },
        Wepay: {
            query: (running) => GET("/api/pay/wepay/query?" + util.forGetParams({running}))(),
            start: (params) => GET("/api/pay/wepay/start?" + util.forGetParams(params))(),
        }
    },

    Receipt: {
        newApplication: (data) => POST(`/api/receipt/new`, data)(),

        getStatDataUser: GET(`/api/receipt/stat-data`),
        getStatDataAdmin: uid => GET(`/api/customer/${uid}/receipt/statv`)(),
    },

    Notification: {
        getList: (page) => GET('/api/notification')(),
        process: (id) => POST(`/api/notofication/${id}`, {r: 1})(),
    },
}