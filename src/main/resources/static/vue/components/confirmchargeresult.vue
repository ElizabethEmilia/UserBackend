<template>
    <div>
        <h1>支付结果反馈</h1>

        <p style="margin-top: 10px;">
            请在打开的新窗口中进行交易，请根据交易情况进行选择。
        </p>

        <div class="button" @click="query">
            已完成付款
        </div>
        <div class="button"  @click="query">
            付款遇到问题
        </div>

    </div>
</template>

<script>
    /**
     * events:
     *    on-cancel:
     */

    export default {
        props: [ 'method' ],
        methods: {
            async query() {
                if (window.config && !window.config.isCustomerPaid)
                    location.href='./'; // 如果用户是从产品列表页面付款的，那么刷新页面
                this.$emit("on-cancel");
            }
        }
    }
</script>

<style scoped lang="less">
    @import "../../css/initstyle.less";

    * {
        font-size: 14px;
    }

    div.button {
        background: #eee;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        border-radius: 4px;
        padding: 5px;
        font-size: 15px;
        margin: 4px;
    }

    div.button:hover {
        background: #f3f3f3;
    }

    div.button:active {
        background: #eee;
    }


</style>