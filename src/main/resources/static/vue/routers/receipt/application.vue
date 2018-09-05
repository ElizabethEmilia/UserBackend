<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>我的开票申请</h3></Divider>
        
        <Select v-model="selected.cid" placeholder="申请公司" style="width: 200px; margin-left: 5px;">
            <Option v-for="(e, i) in companies" :value="e.id" :key="i">{{e.lpname}}</Option>
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
                <ButtonGroup>
                    <Button @click="newApplication">新增</Button>
                    <Button>提交</Button>
                    <Button>删除</Button>
                    
                </ButtonGroup>
            </Col>
        </Row>

        <PagedTable v-if="selected != -1" 
            :columns="columns" 
            data-source="receipt/list"
            :additional-params="searchParams"
         />
        
        <Modal v-model="shouldOpenDialogNew" title="新增" >
            <NewApplicationDialog
                :companies="companies"
                @on-select="selectNewReceiptType"
            />
            <div slot="footer" style="text-align: right">
                <Button type="primary" @click="startNewReceipt">确定</Button>
                <Button type="default" @click="shouldOpenDialogNew = false">取消</Button>
            </div>
        </Modal>

        <Modal v-model="shouldOpenDialogEdit" :title="'申请' + receiptTyString">
            <div style="margin-bottom: 5px;">
                <span class="title-before-input">发票类型 </span>
                <span> {{ receiptTyString }} </span>
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> 申请公司 </span>
                {{ applicationCompanyInfo.lpname }} ({{ applicationCompanyInfo.id }})
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input">税金预交率 </span>
                <span> {{ applicationCompanyInfo.preTaxRetio*100 }}% </span>
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />客户名称 </span>
                <Input v-model="applicationData.cusName" placeholder="" clearable style="width: 200px" />
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />开票金额 </span>
                <MoneyInput v-model="applicationData.recAmount" style="width: 200px"></MoneyInput>
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> 预交税金 </span>
                ￥{{ applicationCompanyInfo.preTaxRetio*applicationData.recAmount }}
            </div>
            <div slot="footer" style="text-align: right">
                <Button type="primary" @click="e=>1">确定</Button>
                <Button type="default" @click="shouldOpenDialogEdit = false">取消</Button>
            </div>
        </Modal>
    </Card>
</template>

<script>
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { receiptType, receiptStatus, Integers } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';
import companyVue from '../company.vue';
import receiptstate from './receipt_s.js';
import NewApplicationDialog from './dialog/new.vue';
import API from '../../../js/api.js';
import MoneyInput from '../../components/moneyinput.vue';
    
export default {
    components: {
        PagedTable, NewApplicationDialog, MoneyInput,
    },
    props: [ 'cid' ],
    data: () => ({
        companies: [

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
                {title:"状态",render:(h,p)=>h('span',{},receiptStatus[self.d[p.index].status])}, //status
                {title:"驳回原因",key:"reason"},
                {title:"提交时间",key:"tmSubmit"},
                {title:"确认时间",key:"tmVallidate"},
                { 
                    title: '操作', 
                    render: (h, p) => h('div', util.State.render(self.d[p.index].status, h, p, self, "/api/receipt",receiptstate.receiptStateMap, receiptstate.receiptActionName)),
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

        shouldOpenDialogNew: false,
        selectedNew: { cid: -1, ty: 0 },
        userInfo: null,

        // 开具新发票的属性
        shouldOpenDialogEdit: false,
        receiptTyString: '',
        applicationCompanyInfo: {},
        applicationData: {
            "cid": "",
            "recType": "",
            "cusName": "",
            "recAmount": 0,
            "address": "",
            // 要求后端查询预交税率
        }

    }),
    methods: {
        async getCompanies() {
            try {
                let result = await $.ajax('/api/company/list');
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
        // 开具新的发票
        async startNewReceipt() {
            if (this.selectedNew.cid < 0 || this.selectedNew.ty < 0)
                return util.MessageBox.Show(this, "请选择要开具的发票的公司类型");
            try {
                let user = this.userInfo != null ? this.userInfo :
                    (this.userInfo = await API.Account.getBasicInfo());
                if (user.paid === 0 && this.selectedNew.ty === Integers.ReceiptType.SPECIAL) {
                    return util.MessageBox.Show(this, "你没有权限开具此类发票");
                }
                this.receiptTyString = receiptType[this.selectedNew.ty];
                // 获取报税频率
                this.applicationCompanyInfo = this.companies.filter(e=>e.id===this.selectedNew.cid)[0];
                let vatr_freq = this.applicationCompanyInfo.vatrFreq;
                if (vatr_freq === -1) {
                    return util.MessageBox.Show(this, "你公司的报税频率还未完善，无法开具发票");
                }
                this.shouldOpenDialogNew = false;
                this.shouldOpenDialogEdit = true;
            }
            catch(err) {
                console.error(err);
            }
        }
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
