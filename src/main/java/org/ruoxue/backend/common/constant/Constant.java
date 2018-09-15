package org.ruoxue.backend.common.constant;

public final class Constant {
    /*
    客户表（会员类别）：

    0 - 个人用户
    1 - 企业用户
     */
    public class CustomerType {
        public static final int PERSONAL = 0;
        public static final int ENTERPRISE = 1;
    }

    /*
    客户表（可开票种类）

    0 - 不可开票
    1 - 专用票
    2 - 普通票
    3 - 普通票和专用票
     */
    public class CustomerIssuableReceiptType {
        public static final int NEITHER = 0;
        public static final int SPECICAL = 1;
        public static final int NORMAL = 2;
        public static final int BOTH = 3;
    }

    /*
    权限值
     */
    public class ModulePrivilege {
        // 客户管理模块
        public static final int CUSTOMER = 1;
        // 管理员管理模块
        public static final int ADMINISTRATOR = 2;
    }

    /*
    交易表（支付方式）

    0 - 在线-支付宝
    1 - 在线-微信
    2 - 在线-余额（对于充值不可用）
    3 - 在线-转账（不可用）
    4 - 对公-银行
    5 - 对公-现金
    6 - 对公-支付宝
    7 - 其他
     */
    public class PaymentMethod {
        public final int ONLINE_ALIPAY = 0;
        public final int ONLINE_WECHAT = 1;
        public final int ONLINE_BALANCE = 2;
        public final int ONLINE_TRANSACTION = 3;
        public final int PUBLIC_BANK = 4;
        public final int PUBLIC_MONEY = 5;
        public final int PUBLIC_ALIPAY = 6;
        public final int OTHERS = 7;
    }

    /*
    交易表（交易状态）

    0 - 未付款
    1 - 充值成功
    2 - 充值失败
     */
    public class ExchangeStatus {
        public final int UNPAIED = 0;
        public final int SUCCESS = 1;
        public final int FAILED = 2;
    }

    /*
    交易表（交易类型）

    0 - 支出
    1 - 收入
     */
    public class ExchangeType {
        public static final int OUTCOME = 0;
        public static final int INCOME = 1;
    }

    /*
    对公充值表（订单状态）

    0 - 待确认
    1 - 已确认
    2 - 已取消
     */
    public class PublicChargeState {
        public static final int WAITING = 0;
        public static final int CONFIRMED = 1;
        public static final int CANCELED = 2;
    }

    /*
    订单表（订单状态）

    0 - 待支付
    1 - 已支付
    2 - 已取消
     */
    public class PaymentStatus {
        public static final int UNPAIED = 0;
        public static final int PAIED = 1;
        public static final int CANCELED = 2;
    }

    /*
    开票表（发票类型）

    0 - 增值税普通发票
    1 - 增值税专用发票
     */
    public class ReceiptType {
        public static final int NORMAL = 0;
        public static final int SPECIAL = 1;
    }

    /*
    开票表（状态）

    0 - 待提交
    1 - 待审核
    2 - 审核通过
    3 - 审核未通过
    4 - 已开票（和客户确认此状态是否存在）
     */
    public class RECEIPT_STATUS {
        public static final int TO_BE_SUBMITTED = 0;
        public static final int PENDING = 1;
        public static final int PASSED = 2;
        public static final int REFUSED = 3;
        public static final int ISSUED = 4;
    }

    /*
    预计年销售额范围表（税金预交档次状态）

    0 - 待选档
    1 - 已选档
    2 - 年销售额范围变更
    3 - 税金预交差额补交
    4 - 撤回变更
    5 - 已失效（可能没有此状态，但以防万一，添加一个）
     */
    public class PreTaxStallsStatus {
        public static final int UNSELECTED = 0;
        public static final int SELECTED = 1;
        public static final int RANGE_CHANGEED = 2;
        public static final int PAID_DIFFERENCE = 3;
        public static final int WITHDREW = 4;
        public static final int INACTIVATED = 5;
        public static final int REQUESTED_WITHDRAW = 6;
        public static final int DIFFERENCE_TO_PAY = 7;
    }

    /*(
    预计年销售额范围表（预计年销售额范围）

    区间前开后闭

    [0] - 0-30000
    [1] - 30000-90000
    [01] - 0-90000

    [2] - 90000-360000
    [3] - 360000-1000000
    [23] - 90000-1000000

    [4] - 1000000-2000000
    [5] - 2000000-3000000
    [6] - 3000000-4000000
    [7] - 4000000-4500000
    [4567] - 1M-4.5M
     */
    public class SallyRange {
        public static final int LESS_THAN_30K = 1 << 0;
        public static final int BETWEEN_30K_AND_90K = 1 << 1;
        public static final int BETWEEN_90K_AND_360K = 1 << 2;
        public static final int BETWEEN_360K_AND_1M = 1 << 3;
        public static final int BETWEEN_1M_AND_2M = 1 << 4;
        public static final int BETWEEN_2M_AND_3M = 1 << 5;
        public static final int BETWEEN_3M_AND_4M = 1 << 6;
        public static final int BETWEEN_4M_AND_4500K = 1 << 7;

        public static final int LESS_THAN_90K = LESS_THAN_30K | BETWEEN_30K_AND_90K;
        public static final int BETWEEN_90K_AND_1M = BETWEEN_90K_AND_360K | BETWEEN_360K_AND_1M;
        public static final int BETWEEN_1M_AND_4500K = BETWEEN_1M_AND_2M | BETWEEN_2M_AND_3M | BETWEEN_3M_AND_4M | BETWEEN_4M_AND_4500K;
    }

}
