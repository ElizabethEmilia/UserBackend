<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>开票申请</h3></Divider>
        
        <Select v-model="selected.cid" placeholder="申请公司" style="width: 200px; margin-left: 5px;">
            <Option v-for="(e, i) in companies" :value="e.id" :key="e.id">{{e.name}}</Option>
        </Select>

        <Select v-model="selected.type" placeholder="发票类型" style="width: 130px;  margin-left: 5px;">
            <Option v-for="(e, i) in receiptType" :value="i" :key="i">{{e}}</Option>
        </Select>

        <Select v-model="selected.status" placeholder="发票状态" style="width: 130px;  margin-left: 5px;">
            <Option v-for="(e, i) in receiptStatus" :value="i" :key="i">{{e}}</Option>
        </Select>

         <DatePicker v-model='selected.start' type="date" placeholder="起始日期" style="width: 130px; margin-left: 5px;"></DatePicker>

         <DatePicker v-model='selected.end' type="date" placeholder="截止日期" style="width: 130px; margin-left: 5px;"></DatePicker>

        <Row style="margin-top: 10px;">
            <!--Col span="16">
                当前查询 开票金额合计：{{ totalReceiptAmount }}, 预缴税合计： {{ preTaxTotal }}
            </Col-->
            <Col span="8">
                
            </Col>
        </Row>

        <PagedTable
            :columns="columns" 
            :data-source="dataSource"
            :additional-params="searchParams"
         />
        
        
    </Card>
</template>

<script>
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { receiptType, receiptStatus } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';
import receiptSM from '../receipt/receiptstate.js';
    
export default {
    components: {
        PagedTable
    },
    props: [ 'cid', 'uid' ],
    data: () => ({
        companies: [

        ],
        columns() {
            let self = this;
            return [
                {title:"序号",type:"index", width: 70},
                {title:"申请编号",key:"id", width: 140},
                {title:"发票类型",width: 200, render:(h,p)=>h('span',{},receiptType[self.d[p.index].recType])},
                //{title:"公司ID",key:"cid", width: 70},
                {title:"申请公司",key:"cid", width: 250},
                {title:"客户名称",key:"cusName", width: 250},
                {title:"合同名称",key:"agname", width: 250},
                {title:"纳税人识别号",key:"agtaxno", width: 250},
                {title:"开票金额（含税）",key:"recAmount", width: 140},
                {title:"税金预交率",width: 140,render:(h,p)=>h('span',{},""+(self.d[p.index].pretaxRatio*100)+"%")},
                {title:"预交税金",key:"pretax", width: 140},
                {title:"地址",key:"address", width: 250},
                {title:"状态", width: 200, render:(h,p)=>h('span',{},receiptStatus[self.d[p.index].status])}, //status
                {title:"驳回原因",key:"reason", width: 210},
                {title:"提交时间", width: 210, render:(h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(self.d[p.index].tmSubmit)))},
                {title:"确认时间", width: 210, render:(h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(self.d[p.index].tmValidate)))},
                {title:"合同", width: 150, render:(h,p)=>h('span',[ render.link(h,p,'查看合同', function() {
                        window.open('/res/avatar/' + p.row.credit);
                    }) ])},
                {
                    title: '操作', 
                    width: 200,
                    render: (h, p) => h('div', receiptSM.render(self.d[p.index].status, h, p, self, "/api/customer/_/receipt",
                        function(action, params) {
                            if (action === receiptSM.receiptAction.RefuseSubmit) {
                                params.reason = prompt('输入拒绝原因：');
                                if (util.String.isNullOrEmpty(params.reason)) {
                                    alert('没有输入拒绝原因，无法拒绝');
                                    return false;
                                }
                                return true;
                            }
                            return true;
                        }
                    )),
                }
            ];
        },
        selected: {
            cid: '',
            type: '',
            status: '',
            start: '',
            end: '',
        },
        receiptType,
        receiptStatus,
        receiptData: [],

        statData: { amount: -1, tax: -1 },

    }),
    methods: {
        async getCompanies() {
            let result = await $.ajax('/api/customer/' + this.uid + '/company/list');
            if (result.code === 0)
                this.companies = result.data;
        },
    },
    computed: {
        totalReceiptAmount() {
            if (this.statData.amount === -1) {
                return '--';
            }

            return Number(this.statData.amount)
                .toFixed(2);
        },
        preTaxTotal() {
            if (this.statData.tax === -1) {
                return '--';
            }

            return Number(this.statData.tax)
                .toFixed(2);
        },
        searchParams() {
            setTimeout(() => this.getStat(), 0);
            let I = this.selected;
            I.start = util.Date.toDateString(I.start);
            I.end = util.Date.toDateString(I.end);
            return util.forGetParams(I);
        },
        dataSource() {
            setTimeout(() => this.getStat(), 0);
            return `customer/${this.uid}/receipt`
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
