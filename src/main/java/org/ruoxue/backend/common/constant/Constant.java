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
        public static final int ONLINE_ALIPAY = 0;
        public static final int ONLINE_WECHAT = 1;
        public static final int ONLINE_BALANCE = 2;
        public static final int ONLINE_TRANSACTION = 3;
        public static final int PUBLIC_BANK = 4;
        public static final int PUBLIC_MONEY = 5;
        public static final int PUBLIC_ALIPAY = 6;
        public static final int OTHERS = 7;
    }

    /*
    交易表（交易状态）

    0 - 未付款
    1 - 充值成功
    2 - 发生异常
    3 - 等待付款机构处理结果
     */
    public class ExchangeStatus {
        public static final int UNPAIED = 0;
        public static final int SUCCESS = 1;
        public static final int FAILED = 2;
        public static final int WAITING = 3;
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
    3 - 待确认
     */
    public class PaymentStatus {
        public static final int UNPAIED = 0;
        public static final int PAIED = 1;
        public static final int CANCELED = 2;
        public static final int NOT_CONFIRMED = 3;
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

    0 - 已提交
    1 - 已提交，待审核
    2 - 已审核，待分配
    3 - 已分配（分配），待开票
    4 - 已分配（自取），待开票
    5 - 已开票。待打包
    6 - 已打包,待核对
    7 - 已核对，已寄送
    8 - 已签收
    9 - 已驳回，待提交
    10 - 已开票 已作废
    11 - 已驳回，待打包
     */
    public class RECEIPT_STATUS {
        public static final int Saved = 0;
        public static final int Submitted = 1;
        public static final int Checked = 2;
        public static final int DistributedDistrib = 3;
        public static final int DistributedSelf = 4;
        public static final int Receipted = 5;
        public static final int Packed = 6;
        public static final int VerifiedAndSent = 7;
        public static final int ReceivedCompleted = 8;
        public static final int RefusedWaitingSubmit = 9;
        public static final int Abondoned = 10;
        public static final int RefusedWaitingPacking = 11;
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
        public static final int LESS_THAN_360K = 1 << 1;
        public static final int BETWEEN_360K_AND_1M = 1 << 2;
    }

    public class LogUsers {
        public static final int SYSTEM = -1;
    }

    public class LogClass {
        public static final int NORMAL = 0;
        public static final int MIDIUM = 1;
        public static final int HIGHER = 2;
        public static final int DANGEROUS = 3;
        public static final int IMPORTANT = 3;

    }

    public class ChargeDestination {
        public static final int PACK = 0;
        public static final int TAX = 1;
        public static final int OTHER = 2;
    }

    public class YearlySaleAmountStatus {
        public static final int UNDELECTED = 0; // 系统初始化该状态
        public static final int SELECTED = 1;   // 用户手动选择
        public static final int MODIFIED = 2;   // 用户修改
        public static final int SHOULD_WITHDRAW = 3; // 后台请求撤回
        public static final int SHOULD_COMPLEMENT = 4; // 后台修改金额
    }

}
