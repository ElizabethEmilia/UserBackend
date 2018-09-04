import $ from '../../../js/ajax';

const states = {
    Saved: 0, // 已提交
    Submitted: 1, // 已提交，待审核
    Checked: 2, // 已审核，待分配
    DistributedDistrib: 3, // 已分配（分配），待开票
    DistributedSelf: 4, // 已分配（自取），待开票
    Receipted: 5, // 已开票。待打包
    Packed: 6, // 已打包,待核对
    VerifiedAndSent: 7, // 已核对，已寄送
    ReceivedCompleted: 8, // 已签收
    RefusedWaitingSubmit: 9, // 已驳回，待提交
    Abondoned: 10, // 已开票 已作废
    RefusedWaitingPacking: 11, // 已驳回，待打包
}

const receiptAction = {
    Submit: "submit",
    RefuseSubmit: "refuse-submit",
    Accept: "accept",
    DistribDist: "distrib-dist",
    DistribSelf: "distrib-self",
    Receipt: "receipt",
    Discard: "discard",
    Pack: "pack",
    Send: "send",
    SelfRecv: "selfrecv",
    RefusePacking: "refuse-packing",
    Receive: "recv",
}

const receiptActionName = {
    [receiptAction.Submit]: "提交",
    [receiptAction.RefuseSubmit]: "驳回",
    [receiptAction.Accept]: "通过",
    [receiptAction.DistribDist]: "分配（代开）",
    [receiptAction.DistribSelf]: "分配（自取）",
    [receiptAction.Receipt]: "开票",
    [receiptAction.Discard]: "作废",
    [receiptAction.Pack]: "打包",
    [receiptAction.Send]: "核对并寄送",
    [receiptAction.SelfRecv]: "自取",
    [receiptAction.RefusePacking]: "驳回",
    [receiptAction.Receive]: "已签收",
}

let receiptStateMap = {
    [states.Saved]: {
        [states.Submitted]: receiptAction.Submit,
    },
    [states.VerifiedAndSent]: {
        [states.ReceivedCompleted]: receiptAction.Receive,
    },
    [states.Packed]: {
        [states.ReceivedCompleted]: receiptAction.SelfRecv,
    },
}

export default {
    states,
    receiptStateMap,
    receiptAction,
    receiptActionName,
}