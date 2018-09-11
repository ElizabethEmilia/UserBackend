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
    Customer: {
        deleteUser: (uid) => POST('/api/customer/${uid}/delete', __rd_post_body)(),

        Company: {
            newCompany: (uid, data) => POST(`/api/customer/${uid}/company/new`, data)(),
            deleteCompany: (cid, uid='_') => POST(`/api/customer/${uid}/company/${cid}/delete`, __rd_post_body),

        }
    },
    Account: {
        getBasicInfo: GET("/api/account/basic"),
    },

    Company: {
        getCount: GET("/api/company/count"),
        getList: GET("/api/company/list"),

        SetupStates: {
            update: (stateArray) => Settings_Update('setup_states', stateArray.join(',')),
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
        getList: GET("/api/role/list"),
        remove: roleid => DELETE("/api/role/" + roleid)(),
        get: roleid => GET("/api/role/" + roleid)(),
        modify: (roleid, data) => POST(`/api/role/${roleid}`, data)(),
        addNew: (data) => POST('/api/role/new', data)(),
    },
}