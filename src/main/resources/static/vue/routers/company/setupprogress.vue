<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>公司设立进度</h3></Divider>
        
        <PagedTable  :columns="columns" :data-source="`company/${cid}/setup`" />
        <div style="margin-top: 20px;">

        </div>
    </Card>
</template>

<script>
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import PagedTable from '../../pagedTable.vue';
    
export default {
    props: [ 'cid' ],
    components: {
        PagedTable
    },
    data: () => ({
        columns() { 
            return [
                { title: '序号', type: 'index', width: 70 },
                { title: '时间', width: 1+80, render: (h,p)=>h('span', {}, util.Date.toTimeStringFromTimestamp(p.row.tm) )}, //string
                { title: '状态', key: 'status' }, /// string
                { title: '备注', key: 'note' } // string
            ];
        }
    }),
    methods: {
    },
}
</script>

<style>

</style>
