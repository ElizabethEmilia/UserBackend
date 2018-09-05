import $ from './ajax';
import util from './util.js'

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

export default {
    // 公司相关的请求
    Account: {
        getBasicInfo: GET("/api/account/basic"),
    },
    Company: {
        getCount: GET("/api/company/count"),
        getList: GET("/api/company/count"),
    }
}