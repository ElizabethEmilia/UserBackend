<template>
    <Card class="card-margin" :title="">

    </Card>
</template>

<script>
    import '../../css/style.less';
    import HtmlEditor from 'vue-html-editor';

    export default {
        name: "Template",
        components: {
            HtmlEditor,
        },
        data: () => ({
            title: '编辑模板',

            content: [
                {
                    name: "合同模板",
                    settingName: "template_license",
                    vars: [
                        { name: "orderID", stands: "订单编号" },
                        { name: "companyName", stands: "公司名称" },
                        { name: "companyID", stands: "公司ID(系统内部表示)" },
                        { name: "customerName", stands: "客户名称" },
                        { name: "customerID", stands: "客户ID(系统内部表示)" },
                        { name: "amount", stands: "订单价格(精确到小数点后两位)" },
                        { name: "year", stands: "订单日期（年份）" },
                        { name: "month", stands: "订单日期（月份）" },
                        { name: "day", stands: "订单日期（日）" },
                        { name: "date", stands: "订单日期(yyyy年MM月dd日)" },
                        { name: "name", stands: "订单名称" },
                    ]
                },
                {
                    name: "服务条款模板",
                    settingName: "template_terms",
                    vars: [  ],
                }
            ]
        }),

    }
</script>

<style scoped>

</style>