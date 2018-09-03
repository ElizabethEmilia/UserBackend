<template>
    <!-- 客户列表 -->
    <Card title="客户管理"  class="card-margin">
        <PagedTable :columns="columns" v-if="typeof showList === 'undefined' || showList" data-source="customer" />

    </Card>
</template>

<script>

/*
  选择客户组件：
    事件：
        on-select(客户所有信息)  在选择客户管理时触发 
        on-list()   在展开list时触发
    参数：
        show-list   控制是否应该展开list (bool)
*/

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { industry, receiptStatus, memberType } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';

export default {
    components: {
        PagedTable, 
    },
    props: [
        'showList'
    ],
    data: () => ({
        cdata: {
            
        },
        columns() {
            let self = this;
            return [
                { title:'客户ID', key: 'id' },
                { title:'客户类型',render:(h,p)=>h('span',{},memberType[self.d[p.index].type]) }, // type
                { title:'行业', key: 'industry' },
                { title:'手机号码', key: 'phone' },
                { 
                    title: '地址', 
                    render: (h,p)=>h('span',{}, self.d[p.index].province + self.d[p.index].city + self.d[p.index].district ), 
                },
                { 
                    title: '操作', 
                    key: 'action', 
                    width: 150,
                    render: (h, p) => {
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click: () => {
                                        let info = this.d[p.index];
                                        this.$emit('on-select', info);
                                    }
                                }
                            }, '管理')
                        ]);
                    }
                }
            ];
        }
    }),
}
</script>
