<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>预计年销售额范围管理</h3></Divider>

        <Alert type="success">
            温馨提示：<br/>

            该页面是为了满足客户在不同销售额情况下的税金预交管理需求，详细规则如下：<br/>

            一档（3%）：适用于月销售额小于等于3万或季销售额小于等于9万，没有开具增值税专用发票需求的客户。<br/>

            二档（6%）：适用于预计年销售额大于36万且小于等于100万，或预计年销售额小于等于36万但月销售额大于3万或季度销售额大于9万的客户。<br/>

           <span style="color: red">注：请您根据实际情况选择适用档次，否则有可能影响发票开具和税金缴纳。<br />
               如开票金额与预选税金档位不等，需联系客服进行修改（注：一个服务年度只能申请一次调档）</span>
        </Alert>

        <div class="ft13">
            <div style="margin: 10px 0 5px 0;">
                <Select style="width: 200px;" placeholder="选择公司" v-model="selected">
                    <Option
                            v-for="(e, i) in company"
                            :key="i" :value="i"
                    >{{ e.name }}</Option>
                </Select>
            </div>

            <div style="margin: 10px;">
                <Button type="success" @click="openPreselectDialog">年销售额范围预选</Button>
                <!--Button type="success" @click="openReselectDialog">年销售额范围变更</Button-->
                <Button type="success" @click="complement">税金预交差额补交</Button>
                <Button type="success" @click="withdrawChanges">撤回变更</Button>
            </div>

            <Tabs v-model="res_url" >
                <TabPane label="预选操作记录" name="current"></TabPane>
                <TabPane label="历史记录" name="all"></TabPane>
            </Tabs>

            <div v-if="res_url === 'all'" style="margin-bottom: 10px;">
                <Select v-model="param.status" placeholder="状态" style="width: 130px;  margin-left: 5px;">
                    <Option v-for="(e, i) in expectedSalesStatus" :value="i" :key="i">{{e}}</Option>
                </Select>

                <DatePicker v-model='param.from' type="date" placeholder="起始日期" style="width: 130px; margin-left: 5px;"></DatePicker>

                <DatePicker v-model='param.to' type="date" placeholder="截止日期" style="width: 130px; margin-left: 5px;"></DatePicker>
            </div>

            <PagedTable
                    ref="dt"
                    :data-source="tableDataSource"
                    :columns="columns"
                    :additional-params="searchParams"
            ></PagedTable>

            <Modal
                    :title="dialogTitle"
                    @on-ok="val => isReselect ? processModify() : processPreselect()"
                    v-model="dialogVisible"
                    :width="800"
            >
                <PreSelect
                        v-if="dialogVisible"
                        :obj="company[selected]"
                        @on-change="val => company[selected] && (company[selected].ysaRange = val)" />
            </Modal>
        </div>

    </Card>
</template>

<script>
    import PagedTable from '../../pagedTable.vue';
    import PreSelect from './preselect.vue';
    import $ from '../../../js/ajax.js';
    import util from '../../../js/util.js';
    import API from '../../../js/api.js';
    import { Integers, ysaRange, expectedSalesStatus } from '../../../constant.js';

    const Integer = Integers;

    function hideAdminNames(name) {
        let x = (name).match(/^(.*)-(.*)$/);
        let r = x[1];
        let n = x[2];
        let l = n[0];
        return r.indexOf('管理员') >= 0 ? r + '-' + l + '**' : name;
    }

    export default {
        components: {
            PagedTable, PreSelect,
        },
        data: () => ({
            columns() {
                return [
                    { title: '序号', type: 'index' },
                    { title: '预计年销售额范围', render: (h,p)=>h('span', {}, ysaRange[p.row.ysaRange]) },
                    { title: '税金预缴率', render: (h,p)=>h('span', {}, !isNaN(100 * p.row.preTaxRatio) ? parseInt( 100 * p.row.preTaxRatio ) + '%':'') },
                    { title: '税金预交档次状态', render: (h,p)=>h('span', {}, expectedSalesStatus[p.row.status]) },
                    { title: '税金预交率生效时间', render: (h,p)=>h('span', {}, util.Date.toTimeStringFromTimestamp(p.row.tmActivate)) },
                    { title: '税金预交率失效时间', render: (h,p)=>h('span', {}, util.Date.toTimeStringFromTimestamp(p.row.tmInactivate)) },
                    { title: '操作人', key1: 'oper', render: (h,p)=>h('span', {}, hideAdminNames(p.row.oper)) },
                    { title: '操作时间', render: (h,p)=>h('div', {}, util.Date.toTimeStringFromTimestamp(p.row.tmOp)) },
                ];
            },
            param: {
                status: '',
                from: '',
                to: '',
            },
            company: [],
            selected: -1,
            res_url: "current",

            dialogTitle: '',
            dialogVisible: false,
            isReselect: false,

            expectedSalesStatus,
        }),
        methods: {
            async getCompany() {
                try {
                    let r = util.Objects.convUnderlineToHampObjectArray( await API.Company.getList() );
                    this.company = r;
                }
                catch (e) {
                    console.log(e);
                }
            },
            async processPreselect() {
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                try {
                    await API.Tax.PreSelect.preSelect(this.selectedCompany.id, this.selectedCompany.ysaRange);
                    util.MessageBox.Show(this, "操作成功");
                    this.company[this.selected].ysaStatus = Integer.ExpectedSalesStatus.Selected;
                    this.$refs.dt.refresh();
                }
                catch (err) {
                    console.error(err);
                    util.MessageBox.Show(this, "操作失败, " + err.message);
                }
            },
            async processModify() {
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                try {
                    await API.Tax.PreSelect.reselect(this.selectedCompany.id, this.selectedCompany.ysaRange);
                    util.MessageBox.Show(this, "操作成功");
                    this.company[this.selected].ysaStatus = Integer.ExpectedSalesStatus.Modified;
                    this.$refs.dt.refresh();
                }
                catch (err) {
                    console.error(err);
                    util.MessageBox.Show(this, "操作失败, " + err.message);
                }
            },
            // 撤回更改
            async withdrawChanges() {
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                if (this.selectedCompany.ysaStatus !== Integer.ExpectedSalesStatus.ShouldWithDraw) {
                    return util.MessageBox.Show(this, "您的公司无需撤回变更！");
                }

                try {
                    await API.Tax.PreSelect.withdraw(this.selectedCompany.id);
                    util.MessageBox.Show(this, "操作成功");
                    this.$refs.dt.refresh();
                }
                catch (e) {
                    console.error(e);
                    util.MessageBox.Show(this, "操作失败, " + e.message);
                }
            },
            // 补交税金预交差额
            async complement() {
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                try {
                    let msg = await API.Tax.PreSelect.complement(this.selectedCompany.id);
                    console.log(msg);
                    util.MessageBox.Show(this, msg);
                    this.$refs.dt.refresh();
                    this.company[this.selected].ysaStatus = Integer.ExpectedSalesStatus.Selected;
                }
                catch (e) {
                    console.error(e);
                    util.MessageBox.Show(this, "" + e.message);
                }

            },
            openPreselectDialog() {
                this.isReselect = false;
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                if (this.selectedCompany.ysaStatus !== Integer.ExpectedSalesStatus.Unselected) {
                    return util.MessageBox.Show(this, "只有待选档状态的公司才可以预选年销售额范围！");
                }
                this.dialogTitle = "年销售额范围预选";
                this.dialogVisible = true;
            },
            openReselectDialog() {
                this.isReselect = true;
                if (typeof this.selectedCompany === "undefined") {
                    return util.MessageBox.Show(this, "请选择公司");
                }

                if (this.selectedCompany.ysaStatus === Integer.ExpectedSalesStatus.Unselected) {
                    return util.MessageBox.Show(this, "只有已预选状态的公司才可以预选年销售额范围！");
                }
                this.dialogTitle = "年销售额范围变更";
                this.dialogVisible = true;
            }

        },
        computed: {
            searchParams() {
                return util.forGetParams(Object.assign({}, this.param, {
                    from: util.Date.toDateString(this.param.from),
                    to: util.Date.toDateString(this.param.to),
                }));
            },
            selectedCompany() {
                return this.company[this.selected];
            },
            tableDataSource() {
                if (typeof this.selectedCompany === "undefined")
                    return "company-not-selected";
                return `company/${this.selectedCompany.id}/sales/list/${this.res_url}`;
            }
        },
        mounted() {
            this.getCompany();
        }
    }
</script>

<style scoped>

</style>