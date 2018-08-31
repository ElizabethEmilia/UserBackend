<template>
    <!-- 产品订单 -->
    <Card  class="card-margin">
        <Divider orientation="left"><h3>产品订单</h3></Divider>

        <Alert type="success">
            温馨提示： 没有。
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


    export default {
        data: () => ({
            res_url: 'all',
            columns: [
                { title: '序号', type: 'index' },
                { title: '订单号', key: 'cid'  },
                { title: '公司ID', key: 'cid'  },
                { title: '公司名称', key: 'cname'  },
                { title: '订单金额', key: 'amount'  },
                { title: '订单金额', key: 'paymethod'  },
                { title: '订单状态', key: 'note'  },
                { title: '下单时间', key: 'tm'  }, 
                { title: '支付时间', key: 'tm'  }, 
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
                                    console.log('click', this);
                                    console.log('打印合同 index: ' + params.index);
                                }
                            }
                        }, '打印合同'),
                    ]);
                }
            }
            ]
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
