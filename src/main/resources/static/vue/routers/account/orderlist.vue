<template>
    <!-- 产品订单 -->
    <Card  class="card-margin">
        <Divider orientation="left"><h3>产品订单</h3></Divider>

        <Alert type="success">
            温馨提示： <span v-html="ordertips"></span>。
        </Alert>

         <Tabs :value="res_url" @on-click="tabclick" >
            <TabPane label="全部" name="all"></TabPane>
            <TabPane label="待支付" name="pending"></TabPane>
            <TabPane label="已支付" name="paid"></TabPane>
            <TabPane label="已取消" name="cancelled"></TabPane>
        </Tabs>
        <PagedTable :columns="columns" :data-source="'order/' + res_url" />
    </Card>
</template>

<script>
    import PagedTable from '../../pagedTable.vue';

    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import { ordertips } from '../../../data/tips.js';
    import util from '../../../js/util.js';

    export default {
        data: () => ({
            ordertips,
            res_url: 'all',
            columns: function() { 
                let self = this;
                return [
                { title: '序号', type: 'index' },
                { title: '订单号', key: 'id'  },
                { title: '公司ID', key: 'cid'  },
                { title: '公司名称', key: 'name'  },
                { title: '订单金额', key: 'amount'  },
                { title: '支付方式',width: 160, render: (h,p) => h('span', {}, /*paymentMethod[p.row.paymethod])*/ '余额') }, // key: paymethod
                { title: '订单名称', key: 'note'  },
                { title: '创建时间', render:(h,p)=>h('span',{},util.Date.toTimeStringFromTimestamp(p.row.tmCreate))  },
                { title: '支付时间', render:(h,p)=>h('span',{},util.Date.toTimeStringFromTimestamp(p.row.tmPaid))  },
                { 
                title: '操作', 
                key: 'action', 
                render: (h, params) => {
                    return h('div', [
                        h('a', {
                            props: {
                                href: 'javascript:void(0)',
                            },
                            on: {
                                click() {
                                    window.open('/order/agreement/' + params.row.id);
                                }
                            }
                        }, '查看合同'),
                    ]);
                }
            }
            ];}
        }),
        components: {
            PagedTable
        },
        methods: {
            tabclick(name) {
                this.res_url = name;
            }
        }
    }
</script>
