<template>
    <Card>
        <PagedTable :columns="columns" data-source="system/log" />
    </Card>
</template>

<script>
    import PagedTable from '../pagedTable.vue';
    import { adminTypes } from '../../constant.js';
    import $ from '../../js/ajax.js';
    import util from '../../js/util.js';

    export default {
        components: {
            PagedTable
        },
        data: () => ({
            columns: function() {
                //console.log('func', this);
                let $this = this;
                return [
                    //{ type: 'selection', width: 60, align: 'center' },
                    { title: '序号', type: 'index', width: 100 },
                    { title: '管理员名称', key: 'name', width: 250 },
                    {
                        title: '时间',
                        render: (h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(p.row.tm))),
                        width: 200,
                    },
                    { title: '描述', key: 'description', width: 600 },

                ]
            },
        })
    }
</script>