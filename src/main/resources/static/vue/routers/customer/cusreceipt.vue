<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>开票申请</h3></Divider>
        
        <Select v-model="selected.cid" placeholder="申请公司" style="width: 200px; margin-left: 5px;">
            <Option v-for="(e, i) in companies" :value="e.cid" :key="e.cid">{{e.lpname}}</Option>
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
            <Col span="16">
                当前查询 开票金额合计：{{ totalReceiptAmount }}, 预缴税合计： {{ preTaxTotal }}
            </Col>
            <Col span="8">
                
            </Col>
        </Row>

        <PagedTable v-if="selected != -1" 
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
            { cid: 1, lpname: "大乔科技工作室" },
            { cid: 2, lpname: "小乔科技工作室" }
        ],
        columns() {
            let self = this;
            return [
                {title:"选择",type:"select"}
                ,{title:"序号",type:"index"},
                {title:"申请编号",key:"id"},
                {title:"发票类型",render:(h,p)=>h('span',{},receiptType[self.d[p.index].recType])},
                {title:"公司ID",key:"cid"},
                {title:"申请公司",key:"cid"},
                {title:"客户名称",key:"cusName"},
                {title:"开票金额（含税）",key:"recAmount"},
                {title:"税金预交率",key:"pretaxRatio"},
                {title:"预交税金",key:"pretax"},
                {title:"状态",width:100,render:(h,p)=>h('span',{},receiptStatus[self.d[p.index].status])}, //status
                {title:"驳回原因",key:"reason"},
                {title:"提交时间",key:"tmSubmit"},
                {title:"确认时间",key:"tmVallidate"},
                { 
                    title: '操作', 
                    width: 200,
                    render: (h, p) => {
                        let state = self.d[p.index].status;
                        console.log('[]', state);
                        return h('div', receiptSM.render(self.d[p.index].status, h, p, self));
                    }
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
            if (this.receiptData.length == 0) {
                return '0.00';
            }

            return String(Number(this.receiptData
                .map(e=>e.amount)
                .reduce((a,b)=>a+b))
                .toFixed(2));
        },
        preTaxTotal() {
            if (this.receiptData.length == 0) {
                return '0.00';
            }

            return String(Number(this.receiptData
                .map(e=>e.preTax)
                .reduce((a,b)=>a+b))
                .toFixed(2));
        },
        searchParams() {
            return util.forGetParams(this.selected);
        },
        dataSource() {
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
