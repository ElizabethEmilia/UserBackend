<template>
    <Card>
        <PagedTable :columns="columns" data-source="admin/list" />
    </Card>
</template>

<script>
import PagedTable from '../pagedTable.vue';
import { adminTypes } from '../../constant.js';
import $ from '../../js/ajax.js';
import util from '../../js/util.js';

let privileges = ['操作员', '管理员']

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
                { title: '管理员名称', key: 'name',  },
                { 
                    title: '权限', 
                    render: (h,p) => h('span', {}, adminTypes[this.tableData[p.index].privilege]),
                    
                },
                { title: '电话号码', key: 'phone' },
                { 
                    title: '操作', 
                    key: 'action', 
                    width: 150,
                    render: (h, params) => {
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click: async () => {
                                        if (!confirm(`确认删除${adminTypes[this.tableData[params.index].privilege]}${this.tableData[params.index].name}吗?`))
                                            return;
                                        let aid = $this.tableData[params.index].wid;
                                        try {
                                            let result = await $.ajax(`/api/admin/${aid}/delete`, 't');
                                            if (result.code == 0) {
                                                util.MessageBox.Success(this, '删除成功');
                                                $this.tableData.splice(params.index);
                                            }
                                            else {
                                                util.MessageBox.Error(this, '删除失败');
                                            }
                                        }
                                        catch(err) {
                                            util.MessageBox.Error(this, '删除失败。');
                                        }
                                    }
                                }
                            }, '删除'),
                            h('span', {}, ' | '),
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click: async () => {
                                        let aid = await $this.tableData[params.index].wid;
                                        /// show dialog
                                        alert('显示aid为' + aid + '的管理员信息');
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