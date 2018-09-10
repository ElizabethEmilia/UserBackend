<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>预计年销售额范围管理</h3></Divider>

        <Alert type="success">
            温馨提示：<br/>

            该页面是为了满足客户在不同销售额情况下的税金预交管理需求，详细规则如下：<br/>

            一档（3%）：适用于月销售额小于等于3万或季销售额小于等于9万，没有开具增值税专用发票需求的客户。<br/>

            二档（6%）：适用于预计年销售额大于36万且小于等于100万，或预计年销售额小于等于36万但月销售额大于3万或季度销售额大于9万的客户。<br/>

            三档（7%）：适用于预计年销售额大于100万且小于等于200万，大于200万且小于等于300万，大于300万且小于等于400万或大于400万且小于等于450万的客户。<br/>

           <span style="color: red">注：请您根据实际情况选择适用档次，否则有可能影响发票开具和税金缴纳</span>
        </Alert>

        <div style="margin: 10px 0 5px 0;">
            <Select style="width: 200px;" placeholder="选择公司">
                <Option
                    v-for="(e, i) in company"
                    :key="i" :value="i"
                >{{ e.lpname }}</Option>
            </Select>
        </div>

        <div style="margin: 10px;">
            <Button type="success">年销售额范围预选</Button>
            <Button type="success">年销售额范围变更</Button>
            <Button type="success">税金预交差额补交</Button>
            <Button type="success">撤回变更</Button>
        </div>

        <PagedTable
            data-source="tax"
            :columns="columns"
            :additional-params="searchParams"
        ></PagedTable>
    </Card>
</template>

<script>
    import PagedTable from '../../pagedTable.vue';
    import $ from '../../../js/ajax.js';
    import util from '../../../js/util.js';
    import API from '../../../js/api.js';

    export default {
        components: {
            PagedTable,
        },
        data: () => ({
            columns() {
                return [
                    { title: '序号', type: 'index' },
                    { title: '公司', type: 'name' },
                    { title: '预计年销售额范围', type: 'range' },
                    { title: '税金预缴率', type: 'preTaxRatio' },
                    { title: '税金预交档次状态', type: 'status' },
                    { title: '税金预交率生效时间', type: 'activeDate' },
                    { title: '税金预交率失效时间', type: 'inactivateDate' },
                    { title: '操作人', type: 'oper' },
                    { title: '操作时间', type: 'tmOp' },
                ];
            },
            param: {
                cid: -1,
            },
            company: [],
        }),
        computed: {
            searchParams() {
                return util.forGetParams(this.param);
            },
            async getCompany() {
                let r = await API.Company.getList();
                this.company = r;
            }
        },
        mounted() {
            this.getCompany();
        }
    }
</script>

<style scoped>

</style>