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
        [states.Saved]: receiptAction.Submit,
    },
    [states.Submited]: {
        [states.RefusedWaitingSubmit]: receiptAction.RefuseSubmit,
        [states.Checked]: receiptAction.Accept,
    },
    [states.Checked]: {
        [states.DistributedDistrib]: receiptAction.DistribDist,
        [states.DistributedSelf]: receiptAction.DistribSelf,
    },
    [states.DistributedDistrib]: {
        [states.Receipted]: receiptAction.Receipt,
    },
    [states.DistributedSelf]: {
        [states.Receipted]: receiptAction.Receipt,
    },
    [states.Receipted]: {
        [states.Abondoned]: receiptAction.Discard,
        [states.Packed]: receiptAction.Pack,
    },
    [states.Packed]: {
        [states.VerifiedAndSent]: receiptAction.Send,
        [states.ReceivedCompleted]: receiptAction.SelfRecv,
        [states.RefusedWaitingPacking]: receiptAction.RefusePacking,
    },
    [states.VerifiedAndSent]: {
        [states.ReceivedCompleted]: receiptAction.Receive,
    },
    [states.RefusedWaitingPacking]: {
        [states.Packed]: receiptAction.Pack,
    }
}

// from , to, render, params
function renderVDOM(f, t, h, p, self) {
    let action = receiptStateMap[f][t];
    // 如果转台转换不存在
    if (typeof action == "undefined")
        return [];
    let DividerVDOM = h('span', {}, ' | ');
    let LinkVDOM = h('a', {
        props: { href: 'javascript:void(0)', },
        on: {
            async click() {
                if (!confirm('确认执行“' + receiptActionName[action] + '”操作吗？'))
                    return;
                let id = self.d[p.index].id;
                try {
                    let r = await $.ajax(`/api/receipt/${id}/${action}`, {id});
                    if (r.code)
                        return alert('操作失败' + r.msg);
                    alert('操作成功');
                    self.refresh();
                }
                catch (err) {
                    alert('操作失败');
                }
            }
        }
    }, receiptActionName[action])
    debugger
    return [ LinkVDOM, DividerVDOM ];
}

// 根据当前状态进行render
function render(state, h, p, self) {
    let to = receiptStateMap[state];
    let kt = Object.keys(to);
    let rA = []
    for (let t of kt)
        rA = [ ...rA, ...renderVDOM(state, t, h, p, self) ];
    return rA;
}

export default {
    states,
    receiptStateMap,
    receiptAction,
    receiptActionName,
    renderVDOM,
    render,
}