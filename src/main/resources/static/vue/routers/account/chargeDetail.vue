<template>
    <!--钱包明细 -->
    <Card  class="card-margin">
         <Divider orientation="left"><h3>钱包明细</h3></Divider>
        <Alert type="success">
            温馨提示： 收支明细包括当前登录账户钱包资金的收入及支出详情。
        </Alert>

         <Tabs :value="res_url" @on-click="tabclick" >
            <TabPane label="全部" name="all"></TabPane>
            <TabPane label="收入" name="income"></TabPane>
            <TabPane label="支出" name="outcome"></TabPane>
        </Tabs>
        {{ '/chargedetail/' + res_url }}
        <PagedTable :columns="columns" :data-source="'chargedetail/' + res_url" />
    </Card>
</template>

<script>
    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import PagedTable from '../../pagedTable.vue';

    export default {
        data: () => ({
            res_url: 'all',
            columns: [
                { title: '序号', type: 'index' },
                { title: '公司ID', key: 'cid'  },
                { title: '公司名称', key: 'cname'  },
                { title: '收支金额', key: 'amount'  },
                { title: '支付方式', key: 'paymethod'  },
                { title: '说明', key: 'note'  },
                { title: '交易时间', key: 'tm'  }, 
                        
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
