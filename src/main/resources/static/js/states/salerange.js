import $ from '../../../js/ajax';

const states = {
    Unselected: 0,
    Selected: 1,
    RangeChanged: 2,
    PaidDifferencce: 3,
    Withdrew: 4,
    Inactivated: 5,
    RequestWithdrew: 6,
    DifferenceToPay: 7,
}

const actions = {
    Preselect: "preselect",
    Reselect: "reselect",
    RequestPay: "request-pay",
    Withdraw: "withdraw",
    Complement: "pay",
}

const actionsName = {
    [actions.Preselect]: "预选",
    [actions.Reselect]: "重选",
    [actions.RequestPay]: "要求补款",
    [actions.Withdraw]: "撤回",
    [actions.Complement]: "补款",
}

let stateMap = {
    [states.Unselected]: {
        [states.Selected]: actions.Preselect,
    },
    [states.Selected]: {
        [states.RangeChanged]: actions.Reselect,
        [states.DifferenceToPay]: actions.RequestPay,
        [states.Withdrew]: actions.Withdraw,
    },
    [states.RangeChanged]: {
        [states.RangeChanged]: actions.Reselect,
        [states.DifferenceToPay]: actions.RequestPay,
        [states.Withdrew]: actions.Withdraw,
    },
    [states.PaidDifferencce]: {
        [states.RangeChanged]: actions.Reselect,
    },
    [states.Withdrew]: {
        [states.RangeChanged]: actions.Reselect,
    },
    [states.Inactivated]: { },
    [states.RequestWithdrew]: { },
    [states.DifferenceToPay]: {
        [states.PaidDifferencce]: actions.Complement,
    }
};

export default {
    states,
    stateMap,
    actions,
    actionsName,
}