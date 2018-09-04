<template>
    <!-- 客户列表 -->
    <Card class="card-margin">
        <p slot="title">
            公司列表
        </p>
        <PagedTable 
            :columns="columns"
             data-source="company"
             @on-select="handleOnSelect"
         />

    </Card>
</template>

<script>

/*
  客户公司组件
    事件：
        on-select(客户公司信息)  在选择客户公司管理时触发 
    
    参数：
        uid:  客户ID
*/

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { industry, receiptStatus, memberType } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';
import CustomerOverview from './customeroverview.vue';

export default {
    components: {
        PagedTable, CustomerOverview
    },
    props: [
        'uid'
    ],
    data: () => ({
        selected: -1, // 选择的客户ID
        cdata: {
            // 已经选择的客户数据    
        },
        columns() {
            let self = this;
            return [
                { title:'客户名', key: 'name' },
                { title:'客户类型',render:(h,p)=>h('span',{},memberType[self.d[p.index].type]) }, // type
                { title:'行业', render:(h,p)=>h('span',{},industry[self.d[p.index].industry]) }, //industry
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
    computed: {
        
    },
    methods: {
        handleOnSelect(cd) {
            this.cdata = cd;
            this.selected = cd.uid;
            this.$emit('on-select', cd);
        },
        // 获取公司
        async getCompany() {
            try {
                let result = await $.ajax(`/api/customer/${ this.cusData.uid }/company/list`);
                if (result.code) {
                    return util.MessageBox.Show(this, '获取公司失败：' + result.msg);
                }
                this.companyCount = result.data.length;
                this.companyList = result.data;
            }
            catch(err) {
                 util.MessageBox.Show(this, '获取公司失败');
            }
        },
    },
    mounted() {
        this.getCompany();
    }
}
</script>
