<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>公司设立进度</h3></Divider>
        
        <PagedTable v-if="selected != -1" :columns="columns" :data-source="`company/${cid}/setup`" />
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
    },
}
</script>

<style>

</style>
