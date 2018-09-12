const tAdmin = {
    id: 0,
    name: "",
    lid: 0,
    role: 0,
    group: 0,
    wid: "",
    phone: "",
    createdate: "",
    modifieddate: "",
    status: 0
};


const tComCert = {
    id: 0,
    cid: 0,
    uid: 0,
    certNo: "",
    certName: "",
    certImg: "",
    tmUpd: "",
    status: 0
};

const tComSetProgress = {
    id: 0,
    cid: 0,
    uid: 0,
    tm: "",
    status: "",
    note: ""
};

const tCompany = {
    id: 0,
    uid: 0,
    parent: -1,
    lpname: "",
    taxType: 0,
    vatType: 0,
    ysaRange: 0,
    preTaxRatio: 0,
    vaTaxRatio: 0,
    vatrFreq: 0,
    cbTax: 0,
    eaTax: 0,
    leaTax: 0,
    riverTax: 0,
    entOrgType: 0,
    invType: 0,
    businessType: "",
    taxPackStart: "",
    taxPackEnd: "",
    oriTaxPackStart: "",
    oriTaxPackEnd: "",
    tmFirstEc: "",
    status: "",
    name: "",
    ysaStatus: 0,
};

const tConfig = {
    name: "",
    value: "",
    friendlyname: "",
    visible: 0,
    type: 0,
    description: "",
    permission: 0
};

const tCustomer = {
    uid: 0,
    lid: 0,
    name: "",
    type: 0,
    industry: 0,
    phone: "",
    email: "",
    wechat: "",
    qq: "",
    fax: "",
    province: "",
    city: "",
    district: "",
    address: "",
    avatar: "",
    otherContact: "",
    paid: 0,
    balance: 0,
    recType: 0,
    regDate: "",
    status: 0
};

const tDictAreas = {
    id: 0,
    cityCode: 0,
    areaCode: 0,
    areaName: ""
};

const tDictCities = {
    id: 0,
    provinceCode: 0,
    cityCode: 0,
    cityName: ""
}

const tDictProvinces = {
    id: 0,
    provinceCode: 0,
    provinceName: ""
};

const  tExchange = {
    id: 0,
    running: 0,
    uid: 0,
    cid: 0,
    amount: 0,
    paymethod: 0,
    note: "",
    tm: "",
    state: 0,
    type: 0
};

const tExpectedIncome = {
    id: 0,
    cid: 0,
    uid: 0,
    ysaRange: 0,
    preTaxRatio: "",
    status: 0,
    tmActivate: "",
    tmInactivate: "",
    oper: "",
    tmOp: ""
};

const tLogs = {
    id: 0,
    aid: 0,
    tm: "",
    description: "",
    cls: 0
};

const tOperationLog = {
    id: 0,
    logtype: "",
    logname: "",
    userid: 0,
    classname: "",
    method: "",
    createtime: "",
    succeed: "",
    message: ""
};

const tOrder = {
    id: 0,
    cid: 0,
    type: 0,
    amount: 0,
    status: 0,
    tmCreate: "",
    tmPaid: ""
};

const tPublicCharge = {
    id: 0,
    uid: 0,
    type: 0,
    amount: 0,
    name: "",
    account: "",
    bank: "",
    credit: "",
    status: 0,
    tmCreate: "",
    tmConfirm: ""
};

const tReceipt = {
    id: 0,
    uid: 0,
    cid: 0,
    recType: 0,
    cusName: "",
    recAmount: 0,
    pretaxRatio: 0,
    pretax: 0,
    status: 0,
    reason: "",
    tmSubmit: "",
    tmVallidate: "",
    address: ""
};

const tReceiptStat = {
    id: 0,
    uid: 0,
    cid: 0,
    income12: 0,
    amountNormal: 0,
    timeNormal: 0,
    countNormal: 0,
    amountSpec: 0,
    timeSpec: 0,
    countSpec: 0,
    curPretax: 0,
    aid: 0,
    tmModify: "",
    hashOriginal: ""
};

const tSignin ={
    id: 0,
    password: "",
    token: "",
    salt: "",
    msgcode: "",
    role: 0
};

const tTaxAccount = {
    id: 0,
    uid: 0,
    cid: 0,
    tyAptRatio: 0,
    balance: 0
};

const tRole = {
    name: '',
    value: 0,
    id: 0,
    templateID: 0,
    remark: '',
}

export default {
    tAdmin,
    tComCert,
    tComSetProgress,
    tCompany,
    tConfig,
    tCustomer,
    tDictAreas,
    tDictCities,
    tDictProvinces,
    tExchange,
    tExpectedIncome,
    tLogs,
    tOperationLog,
    tOrder,
    tPublicCharge,
    tReceipt,
    tReceiptStat,
    tSignin,
    tTaxAccount,
    tRole,
};