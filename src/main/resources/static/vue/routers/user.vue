<template>
    <Card>
        <PagedTable :columns="columns" data-source="admin/list" />
    </Card>
</template>

<script>
import PagedTable from '../pagedTable.vue';
import { adminTypes } from '../../constant.js';

let privileges = ['操作员', '管理员']

export default {
    components: {
        PagedTable
    },
    data: () => ({
        columns: function() {  
            //console.log('func', this);
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: '序号', type: 'index' },
                { title: '管理员名称', key: 'name' },
                { 
                    title: '权限', 
                    render: (h,p) => h('span', {}, adminTypes[this.tableData[p.index].privilege])
                },
                { title: 'wid', key: 'wid' },
                { title: '电话号码', key: 'phone' },
                { 
                    title: '操作', 
                    key: 'action', 
                    render: function(h, params) {
                        console.log(h, params, this);
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click() {
                                        console.log('click', this);
                                        console.log('Cancel index: ' + params.index);
                                    }
                                }
                            }, '删除'),
                            h('span', {}, ' | '),
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click() {
                                        console.log('View index: ' + params.index);
                                    }
                                }
                            }, '查看详情')
                        ]);
                    }
                }
            ]
        },
    })
}
</script>