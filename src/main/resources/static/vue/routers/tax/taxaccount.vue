<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>我的开票申请</h3></Divider>

        <Alert type="success">
            温馨提示：
        </Alert>

        <Tabs>
            <TabPane label="税金账户余额" name="all">
                <Select v-model="params.cid" placeholder="公司名称" style="width: 200px; margin-left: 5px;">
                    <Option v-for="(e, i) in companies" :value="e.id" :key="e.id">{{e.name}}</Option>
                </Select>

                年份：
                <DatePicker type="year" v-model="params.yfrom" style="width: 200px"></DatePicker>
                 -
                <DatePicker type="year" v-model="params.yto" style="width: 200px"></DatePicker>

                <PagedTable
                        :columns="columnsBalance"
                        data-source="to-be-set"
                        :additional-params="paramsGet"
                />
            </TabPane>
            <TabPane label="税金账户明细" name="confirmed">
                <Select v-model="params.cid" placeholder="公司名称" style="width: 200px; margin-left: 5px;">
                    <Option v-for="(e, i) in companies" :value="e.id" :key="e.id">{{e.name}}</Option>
                </Select>

                月份：<DatePicker type="month" v-model="params.yfrom" style="width: 200px"></DatePicker> -
                <DatePicker type="month" v-model="params.yto" style="width: 200px"></DatePicker>

                <Button>查询</Button>

                <div>
                    当前查询 收支差异合计：{{ detail.total }}元， 收入金额合计：{{ detail.income }}元， 支出金额合计：{{ detail.outcome }}元
                </div>

                <PagedTable
                        :columns="columnsDetail"
                        data-source="to-be-set"
                        :additional-params="paramDetailGet"
                />

            </TabPane>
        </Tabs>

    </Card>
</template>

<script>
    import util from '../../../js/util.js';
    import PagedTable from '../../pagedTable.vue';
    import API from '../../../js/api.js';
    import init from '../../../js/init.js';

    export default {
        components: {
            PagedTable,
        },
        props: [ 'cid' ],
        data: () => ({
            companies: [ ],
            params: {
                cid: '',
                yfrom: '',
                yto: '',
            },
            paramDetail: {
                cid: '',
                type: '', // 类型表示是收入还是支出
                mfrom: '', // 缴税月份
                mto: '',
            },
            detail: {
                // 和列表一起统计返回
                total: 0,
                income: 0,
                outcome: 0,
            },
            columnsBalance() {
                console.log('注意  这个就是个统计，删除税金表');
                let self = this;
                return [
                    { title: '序号', type: 'index' },
                    { title: '年份', key: 'year' },
                    { title: '公司ID', key: 'cid' },
                    { title: '公司名称', key: 'name' },
                    { title: '当年有效税金预交率', key: 'tyAptRatio' },
                    { title: '税金余额', key: 'balance' },
                ]
            },
            columnsDetail() {
                return [
                    { title: '序号', type: 'index' },
                    { title: '公司ID', key: 'cid' },
                    { title: '公司名称', key: 'name' },
                    { title: '税金异动流水号', key: 'id' },
                    { title: '申请编号', key: 'rangeid' }, //年销售额范围管理里面申请的ID
                    { title: '开票金额', key: 'receiptAmount' },
                    { title: '税金预交率', key: 'preTaxRetio' },
                    { title: '异动金额', key: 'changeAmount', },
                    { title: '税金收支', key: 'type' }, // 类型
                    { title: '变更类型', key: 'changeType' }, // 年销售额预选的变更类型
                    { title: '账户类型', key: 'accountType' }, // 充值的方式
                    { title: '银行账户', key: 'bankAccount' }, // 充值的银行账户（现在没有）
                    { title: '操作日期', key: 'tmOp' },
                ]
            }
        }),
        methods: {
            async getCompanies() {
                try {
                    let result = await $.ajax('/api/company/list?size=0');
                    if (result.code === 0)
                        this.companies = result.data;
                }
                catch (e) {
                    console.error(e);
                }
            },
            newApplication() {
                this.shouldOpenDialogNew = true;
            },
            selectNewReceiptType(val) {
                this.selectedNew = val;
            },

        },
        computed: {
            totalReceiptAmount() {
                if (this.receiptData.length === 0) {
                    return '0.00';
                }

                return String(Number(this.receiptData
                    .map(e=>e.amount)
                    .reduce((a,b)=>a+b))
                    .toFixed(2));
            },
            preTaxTotal() {
                if (this.receiptData.length === 0) {
                    return '0.00';
                }

                return String(Number(this.receiptData
                    .map(e=>e.preTax)
                    .reduce((a,b)=>a+b))
                    .toFixed(2));
            },

            paramsGet() {
                return util.forGetParams(Object.assign({}, this.params, {
                    yfrom: util.Date.toYearString(this.params.yfrom),
                    yto: util.Date.toYearString(this.params.yto),
                }));
            },
            paramDetailGet() {
                return util.forGetParams(Object.assign({}, this.paramDetail, {
                    mfrom: util.Date.toYearString(this.params.mfrom),
                    mto: util.Date.toYearString(this.params.mto),
                }));
            }
        },
        watch: {

        },
        mounted() {
            this.getCompanies();
        }
    }
</script>

<style>

</style>
