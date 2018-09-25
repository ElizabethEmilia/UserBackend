<template>
    <!-- 产品订单 -->
    <Card  class="card-margin">
        <Divider orientation="left"><h3>产品订单</h3></Divider>

        <Alert type="success">
            温馨提示： {{ ordertips }}。
        </Alert>

        <div style="margin: 10px">
                <Button type="success" @click="newOrder">新增订单</Button>
        </div>

         <Tabs :value="res_url" @on-click="tabclick" >
            <TabPane label="全部" name="all"></TabPane>
            <TabPane label="待支付" name="pending"></TabPane>
            <TabPane label="已支付" name="paid"></TabPane>
            <TabPane label="已取消" name="cancelled"></TabPane>
        </Tabs>
        <PagedTable :columns="columns" :data-source="dataSource" />

        <Modal v-model="newOrderVisible" title="新增订单" :width="800">
            <div  style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        公司
                    </span>
                <Select v-model="orderNew.cid" style="width: 200px;">
                    <Option v-for="(e,i) in companyList" :label="e.name" :value="e.id" :key="i"></Option>
                </Select>
            </div>

            <div  style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        支付方式
                    </span>
                <Select v-model="orderNew.paymethod" style="width: 200px;">
                    <Option v-for="(e,i) in paymentMethod" :label="e" :value="i" :key="i"></Option>
                </Select>
            </div>

            <div style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        订单金额
                    </span>
                <Poptip trigger="focus">
                    <InputNumber :step="100" placeholder="" v-model="orderNew.amount" style="width: 200px; font-size: 14px" />
                    <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber1 }}</span></div>
                </Poptip>

            </div>

            <div  style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        订单名称
                    </span>
                <Input placeholder="" v-model="orderNew.note" style="width: 200px; font-size: 14px" />
            </div>

            <div slot="footer">
                <Button type="success" @click="newOrderOK" :loading="newOrderSaving">确定</Button>
                <Button @click="newOrderVisible = false" :disabled="newOrderSaving">取消</Button>
            </div>

        </Modal>
    </Card>
</template>

<script>
    import PagedTable from '../../pagedTable.vue';
    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import { ordertips } from '../../../data/tips.js';
    import util from '../../../js/util.js';
    import init from '../../../js/init.js';
    import API from '../../../js/api.js';

    let _uid = -1;

    export default {
        props: ['uid'],
        data: () => ({
            newOrderVisible: false,
            newOrderSaving: false,

            orderNew: init.tOrder,

            paymentMethod,

            ordertips,
            res_url: 'all',
            columns: function() { 
                let self = this;
                return [
                { title: '序号', type: 'index' },
                { title: '订单号', key: 'cid'  },
                { title: '公司ID', key: 'cid'  },
                { title: '公司名称', key: 'cname'  },
                { title: '订单金额', key: 'amount'  },
                { title: '支付方式', key: 'paymethod'  },
                { title: '订单名称', key: 'note'  },
                { title: '下单时间', key: 'tm'  }, 
                { title: '支付时间', key: 'tmPaid'  },
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
                                    console.log('打印合同 index: ' + params.index);
                                }
                            }
                        }, '打印合同'),
                    ]);
                }
            }
            ];}
        }),
        components: {
            PagedTable
        },
        computed: {
            dataSource() {
                return 'customer/' + this.uid + '/orders/' + this.res_url
            },

            formatNumber1 () {
                if (this.orderNew.amount === '') return '0';
                function parseNumber(str) {
                    return str.split(/(?=(\d{3})+$)/g).filter((e,i)=>i%2==0).join(',');
                }
                let [str, prec] = (''+this.orderNew.amount).split('.');
                return parseNumber(str) + (prec ? '.'+prec : '');
            },
        },
        methods: {
            tabclick(name) {
                this.res_url = name;
            },

            // 获取公司
            async getCompany() {
                try {
                    let result = await $.ajax(`/api/customer/${ this.uid }/company/list?size=1000`);
                    if (result.code) {
                        return;// alert('获取公司失败：' + result.msg);
                    }
                    //this.loading = false;
                    //this.companyCount = result.data.length;
                    this.companyList = result.data;
                }
                catch(err) {
                    this.loading = false;
                    //util.Debug.ralert('获取公司失败');
                }
            },

            newOrder() {
                this.newOrderVisible = true;
                this.orderNew = init.tOrder;
            },

            async newOrderOK() {
                let I = this.orderNew;

                if (isNaN(Number(I.amount)) || I.amount <= 0) {
                    return util.MessageBox.Show(this,'请输入金额');
                }

                if (util.String.isNullOrEmpty(I.note)) {
                    if (!confirm('你没有输入订单名称，确定不输入吗？'))
                        return;
                }

                if (I.cid <= 0) {
                    return util.MessageBox.Show(this,'请选择公司');
                }

                try {
                    this.newOrderSaving = true;
                    await API.Customer.Orders.newOrder(I.cid, I);
                    util.MessageBox.Show(this, "添加成功");
                    this.newOrderVisible = false;
                    this.newOrderSaving = false;
                }
                catch(e) {
                    console.error(e);
                    util.MessageBox.Show(this, "添加失败");
                    this.newOrderSaving = false;

                }
            },

        },
        mounted() {
            _uid = this.uid;
            this.getCompany();
        }
    }
</script>
