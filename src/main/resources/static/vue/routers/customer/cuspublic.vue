<template>
    <!-- 对公充值 -->
    <Card class="card-margin">
            <Divider orientation="left"><h3>对公充值</h3></Divider>
            
            <Tabs v-model="req_url" @on-click="tabclick">
                <TabPane label="全部" name="all"></TabPane>
                <TabPane label="已确认" name="confirmed"></TabPane>
                <TabPane label="待确认" name="pending"></TabPane>
                <TabPane label="已取消" name="cancelled"></TabPane>
            </Tabs>

            <PagedTable :columns="publicTransferColumnName" :data-source="dataSource + req_url" />
    </Card>
</template>

<script>

/**
 * 参数： 
 *    uid  用户ID 必须
 * */

import PagedTable from '../../pagedTable.vue';
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';

let _uid = -1;

export default {
    props: [ 'uid' ],
    components: {
        PagedTable
    },
    mounted() {
        _uid = this.uid;
    },
    data: () => ({
        publicTransferColumnName() {
            let self = this;
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: '序号', type: 'index', width: 80, },
                { title: '订单编号', key: 'id' },
                { title: '订单类型', render: (h, params) =>  h('span', {}, paymentMethod[this.tableData[params.index].type]) },
                { title: '订单金额', key: 'amount' },
                { title: '订单状态', render: (h, params) => h('span', {}, publicOrderStatus[this.tableData[params.index].status]) },
                { title: '订单时间', render:(h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(p.row.tmCreate)))  },
                //{ title: '确认时间', render:(h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(p.row.tmConfirm)))  },
                { 
                    title: '操作', 
                    key: 'action', 
                    render: (h, params) => {
                        let self = this;
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    async click() {
                                        if (!confirm("确认同意该申请？"))  {
                                            return;
                                        }
                                        let id = self.tableData[params.index].id;
                                        try {
                                            let result = await $.ajax(`/api/customer/${_uid}/publiccharge/${self.d[params.index].id}/confirm`, { r: Math.random() });
                                            if (result.code) {
                                                util.MessageBox.Success(self, '操作失败， ' + result.msg);
                                            }
                                            else {
                                                util.MessageBox.Success(self, '操作成功');
                                                self.refresh();
                                            }
                                        }
                                        catch(err) {
                                            console.log(err);
                                            alert('操作失败, 和服务器通讯发生错误');
                                        }
                                    }
                                }
                            }, self.d[params.index].status === 0 ? '确认' : ''),// 如果是待确认状态，显示此项
                            h('span', {}, self.d[params.index].status === 0 ? ' | ' : ''),

                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    async click() {
                                        if (!confirm("确认要取消该申请吗？"))  {
                                            return;
                                        }
                                        let id = self.tableData[params.index].id;
                                        try {
                                            let result = await $.ajax(`/api/customer/${_uid}/publiccharge/${self.d[params.index].id}/confirm`, { r: Math.random() });
                                            if (result.code) {
                                                util.MessageBox.Success(self, '操作失败 ' + result.msg);
                                            }
                                            else {
                                                util.MessageBox.Success(self, '操作成功');
                                                self.refresh();
                                            }
                                        }
                                        catch(err) {
                                            console.log(err);
                                            util.MessageBox.Success(self, '操作失败, 和服务器通讯发生错误');
                                        }
                                    }
                                }
                            }, self.d[params.index].status === 0 ? '取消' : ''),// 如果是待确认状态，显示此项
                            h('span', {}, self.d[params.index].status === 0 ? ' | ' : ''),

                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    async click() {
                                        window.open('/res/avatar/'+params.row.credit);
                                    }
                                }
                            }, '查看支付凭据')
                        ]);
                    }
                }
            ]
        },

        req_url: "all"
    }),

    methods: {
        tabclick(name) {
            this.res_url = name;
        }
    },

    computed: {
        dataSource() { return "customer/"+this.uid+"/publiccharge/" }
    }
}
</script>
