<template>
<Affix :offset-top="-55">
    <Card  class="card-margin">
         <Divider orientation="left"><h3>选择公司</h3></Divider>
        
        <Select v-model="selected" placeholder="选择你要查看的公司" style="width: 300px;">
            <Option v-for="(e, i) in companies" :value="e.cid" :key="e.cid">{{e.lpname}}</Option>
        </Select>
        <div style="margin-top: 20px;"> </div>
    </Card>
</Affix>
</template>

<script>

/**
 * 选择公司组件
 * 
 * 事件：
 *    on-select-company:  选择的公司ID
 */

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import PagedTable from '../../pagedTable.vue';
    
export default {
    components: {
        PagedTable
    },
    data: () => ({
        companies: [
            { cid: 1, lpname: "大乔科技工作室" },
            { cid: 2, lpname: "大乔科技工作室" }
        ],
        selected: -1,
        columns: [
            { title: '序号', type: 'index' },
            { title: '公司ID', key: 'name' },
            { title: '公司名称', key: 'cid' },
            { title: '时间', key: 'tm' },
            { title: '状态', key: 'status' },
            { title: '备注', key: 'note' }
        ]
    }),
    methods: {
        async getCompanies() {
            let result = $.ajax('/api/company/list');
            if (result.code === 0)
                this.companies = result.data;
        },
    },
    watch: {
        selected(val) {
            this.$emit('on-select-company', val);
        }
    },
    mounted() {
        this.getCompanies();
    }
}
</script>

<style>

</style>
