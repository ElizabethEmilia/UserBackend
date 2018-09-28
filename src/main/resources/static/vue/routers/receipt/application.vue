<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>我的开票申请</h3></Divider>
        
        <Select v-model="selected.cid" placeholder="申请公司" style="width: 200px; margin-left: 5px;">
            <Option v-for="(e, i) in companies" :value="e.id" :key="i">{{e.name}}</Option>
        </Select>

        <Select v-model="selected.type" placeholder="发票类型" style="width: 130px;  margin-left: 5px;">
            <Option v-for="(e, i) in receiptType" :value="i" :key="i">{{e}}</Option>
        </Select>

        <Select v-model="selected.status" placeholder="发票状态" style="width: 130px;  margin-left: 5px;">
            <Option v-for="(e, i) in receiptStatus" :value="i" :key="i">{{e}}</Option>
        </Select>

         <DatePicker v-model='selected.start' type="date" placeholder="起始日期" style="width: 130px; margin-left: 5px;"></DatePicker>

         <DatePicker v-model='selected.end' type="date" placeholder="截止日期" style="width: 130px; margin-left: 5px;"></DatePicker>

        <Row style="margin: 10px;">
            <!--Col span="16">
                当前查询 开票金额合计：{{ totalReceiptAmount }}, 预缴税合计： {{ preTaxTotal }}
            </Col-->
            <Col span="8">
                <ButtonGroup>
                    <Button type="success" @click="newApplication">新增开票申请</Button>
                </ButtonGroup>
            </Col>
        </Row>

        <PagedTable v-if="selected != -1"
                    ref="dt"
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

        <Modal :width="800" v-model="shouldOpenDialogEdit" :title="'申请' + receiptTyString" style="font-size: 13.5px !important;">
            <div style="margin-bottom: 5px;">
                <span class="title-before-input">发票类型 </span>
                <span> {{ receiptTyString }} </span>
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> 申请公司 </span>
                {{ applicationCompanyInfo.name }} ({{ applicationCompanyInfo.id }})
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input">税金预交率 </span>
                <span> {{ applicationCompanyInfo.preTaxRatio*100 }}% </span>
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
                <span class="title-before-input"> <i class="required" />送达地址 </span>
                <Input v-model="applicationData.address" placeholder="" clearable style="width: 200px" />
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />合同名称 </span>
                <Input v-model="applicationData.agname" placeholder="" clearable style="width: 200px" />
            </div>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />纳税人识别号 </span>
                <Input v-model="applicationData.agtaxno" placeholder="" clearable style="width: 200px" />
            </div>
            <p>若自取，请在送达地址栏<a href="javascript:void(0)" @click="applicationData.address='自取'">填入“自取”</a></p>
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> 预交税金 </span>
                ￥{{ applicationCompanyInfo.preTaxRatio*applicationData.recAmount }}
            </div>
            <div  style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        真实的合同
                    </span>

                <div style="position: relative">
                    <UploadFile v-model="applicationData.credit" title="上传合同文件"/>
                </div>
                <p>
                    多个文件请打包成zip或rar压缩包上传。
                </p>
            </div>
            <div slot="footer" style="text-align: right">
                <Button type="primary" @click="submit" :loading="uploading">确定</Button>
                <Button type="default" @click="shouldOpenDialogEdit = false">取消</Button>
            </div>
        </Modal>
    </Card>
</template>

<script>
import util from '../../../js/util.js';
import { receiptType, receiptStatus, Integers } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';
import companyVue from '../company.vue';
import receiptstate from './receipt_s.js';
import NewApplicationDialog from './dialog/new.vue';
import API from '../../../js/api.js';
import render from '../../../js/render.js';
import MoneyInput from '../../components/moneyinput.vue';
import UploadFile from '../../components/uploadfile.vue';

let adinit = {
    "cid": -1,
    "recType": "",
    "cusName": "",
    "recAmount": 0,
    "address": "",
    "credit": '',
    agname: '',
    agtaxno: '',
};
    
export default {
    components: {
        PagedTable, NewApplicationDialog, MoneyInput, UploadFile,
    },
    props: [ 'cid' ],
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
                {title:"合同", width: 150, render:(h,p)=>h('span',[render.link(h,p,'查看合同', function() {
                    window.open('/res/avatar/' + p.row.credit);
                    })])},
                {
                    title: '操作',
                    width: 200,
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

        statData: { amount: -1, tax: -1 },

        shouldOpenDialogNew: false,
        selectedNew: { cid: -1, ty: 0 },
        userInfo: null,

        // 开具新发票的属性
        shouldOpenDialogEdit: false,
        receiptTyString: '',
        applicationCompanyInfo: {},
        applicationData: adinit,

        uploading: false,

    }),
    methods: {
        async getCompanies() {
            try {
                this.companies = util.Objects.convUnderlineToHampObjectArray(await API.Company.getList());
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
                this.receiptTyString = receiptType[this.selectedNew.ty];
                // 获取报税频率
                this.applicationCompanyInfo = this.companies.filter(e=>e.id===this.selectedNew.cid)[0];

                let preTaxRatio = this.applicationCompanyInfo.preTaxRatio;
                if (preTaxRatio === 0) {
                    return util.MessageBox.Show(this, "你公司的未选择预计年收入档位，无法开具发票");
                }

                let vatr_freq = this.applicationCompanyInfo.vatrFreq;
                if (vatr_freq === -1) {
                    return util.MessageBox.Show(this, "你公司的报税频率还未完善，无法开具发票");
                }
                this.applicationData = adinit;
                this.applicationData.cid = this.selectedNew.cid;
                this.applicationData.recType = this.selectedNew.ty;
                this.shouldOpenDialogNew = false;
                this.shouldOpenDialogEdit = true;
            }
            catch(err) {
                console.error(err);
            }
        },
        async getStat() {
            try {
                let d = await API.Receipt.getStatDataUser();
                this.statData = d[0];
            }
            catch(e) {
                console.error(e);
            }
        },
        async submit() {

            try {
                this.applicationData.cid = this.selectedNew.cid;
                this.applicationData.ty = this.selectedNew.ty;
                let d = this.applicationData;
                if (d.cid === -1)
                    return util.MessageBox.Show(this, "请选择公司");
                if (util.String.isNullOrEmpty(d.cusName))
                    return util.MessageBox.Show(this, "请输入客户名称");
                if (d.amount <= 0)
                    return util.MessageBox.Show(this, "请输入金额");
                if (util.String.isNullOrEmpty(d.address))
                    return util.MessageBox.Show(this, "请输入送达地址，若自取，填写“自取”");
                if (util.isNullOrUndefined(d.credit.data))
                    return util.MessageBox.Show(this, "必须上传合同作为附件");
                d.credit = d.credit.data;
                if (util.String.isNullOrEmpty(d.agname))
                    return util.MessageBox.Show(this, "请输入合同名称");
                if (util.String.isNullOrEmpty(d.agtaxno))
                    return util.MessageBox.Show(this, "请输入纳税人识别号");

                this.uploading = true;
                await API.Receipt.newApplication(d);
                util.MessageBox.Show(this, "保存成功");
                this.$refs.dt.refresh();
                this.shouldOpenDialogEdit = false;
                this.uploading = !true;
                this.applicationData = adinit;

            }
            catch(e) {
                console.error(e);
                util.MessageBox.Show(this, "保存成功, " + e.message);
                this.uploading = !true;

            }
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
            return util.forGetParams(Object.assign({}, this.selected, {
                start: util.Date.toDateString(this.selected.start),
                end: util.Date.toDateString(this.selected.end),
            }));
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
