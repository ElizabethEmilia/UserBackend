<template>
    <!-- 对公充值 -->
    <Card class="card-margin">
            <Divider orientation="left"><h3>对公充值</h3></Divider>
            <Alert type="success">
                <span style="font-weight: blod; color: green">温馨提示：</span> 
                <br/><br/>
                1. 对公转账需我司财务确认后到账，请在此新增线下充值订单并上传支付凭证；<br />
                2. 我司银行账号信息如下：
                <p v-for="e in publicBankAccount">
                    <span style="display:inline-block; min-width:90px; margin-right: 10px">
                        {{ e.recommend ? '<推荐>':'' }}
                    </span>
                    开户名称：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.accountName }}
                    </span>
                    开户银行：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.bankName }}
                    </span>
                    银行账号：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.accountNo }}
                    </span>
                
                    
                </p>
            </Alert>

            <Tabs v-model="req_url" @on-click="tabclick">
                <TabPane label="全部" name="all"></TabPane>
                <TabPane label="已确认" name="confirmed"></TabPane>
                <TabPane label="待确认" name="pending"></TabPane>
                <TabPane label="已取消" name="cancelled"></TabPane>
            </Tabs>

            <ButtonGroup style="margin: 15px;">
                <Button @click="shouldNewChargeDialogOpen = true;">新增订单</Button>
                <!--Button>取消订单</Button>
                <Button>查看详情</Button-->
            </ButtonGroup>

            <PagedTable ref="dt" :columns="publicTransferColumnName" :data-source="dataSource + req_url" />
    
            <Modal :width="800" title="新增订单" v-model="shouldNewChargeDialogOpen" @on-cancel="shouldNewChargeDialogOpen = false" >
                <PublicChargeDialog @on-input="inputNewInfo" @on-selectcredit="selectCredit" />
                <div slot="footer">
                    <Button @click="newOrderAdd" type="success" size="middle" long :loading="onProcesssing" >新增订单</Button>
                </div>
            </Modal>

            <Modal :width="800" title="支付凭据" v-model="detailDialog">

            </Modal>
    </Card>
</template>

<script>

import PagedTable from '../../pagedTable.vue';
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import PublicChargeDialog from './dialogs/newpublic.vue';
import { Integers } from '../../../constant';
import publicBankAccount from '../../../data/bank_account.js';

export default {
    components: {
        PagedTable, PublicChargeDialog
    },
    data: () => ({
        detailDialog: false,
        shouldNewChargeDialogOpen: false,
        publicBankAccount,
        publicTransferColumnName() {
            let self = this;
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: '序号', type: 'index', width: 80, },
                { title: '订单编号', key: 'id' },
                { title: '订单类型', render: (h, params) =>  h('span', {}, paymentMethod[this.tableData[params.index].type]) },
                { title: '订单金额', key: 'amount' },
                { title: '订单状态', render: (h, params) => h('span', {}, publicOrderStatus[this.tableData[params.index].status]) },
                { title: '下单时间', render: (h,p)=>h('span',{},util.Date.toTimeStringFromTimestamp(p.row.tmCreate)) , __k: 'tmCreate' },
                { title: '确认时间', render: (h,p)=>h('span',{},util.Date.toTimeStringFromTimestamp(p.row.tmConfirm)), _y: 'tmConfirm' },
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
                                    async click() {
                                        if (!confirm("确认要取消订单吗？"))  {
                                            return;
                                        }
                                        let id = self.tableData[params.index].id;
                                        try {
                                            let result = await $.ajax(`/api/charge/public/${id}/cancel`, { r: Math.random() });
                                            if (result.code) {
                                                util.MessageBox.Show(self, '操作失败 ' + result.msg);
                                            }
                                            else {
                                                util.MessageBox.Success(self, '操作成功');
                                                self.refresh();
                                            }
                                        }
                                        catch(err) {
                                            util.MessageBox.Success(self, '操作失败  和服务器的通讯发生错误' );
                                        }
                                    }
                                }
                            }, params.row.status!==0?'':'取消订单'),
                            h('span', {}, params.row.status!==0?'':' | '),
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

        dataSource: "charge/public/",
        req_url: "all",

        newConfig: { },

        orderInfo: {}, // 新订单的信息
        credit: {},

        onProcesssing: false,
    }),

    methods: {
        tabclick(name) {
            this.res_url = name;
        },
        inputNewInfo(val) {
            console.log(val);
            this.orderInfo = val;
        },
        selectCredit(val) {
            console.log(val);
            this.credit = val;
        },
        async newOrderAdd() {
            if (this.onProcesssing)
                return;
            // 检查输入
            let N =this.orderInfo;
            if (N.type === undefined) {
                util.MessageBox.Show(this, "请选择充值方式");
                return false;
            }
            
            if (util.String.isNullOrEmpty(N.name) ||
                N.tmCreate === '' || N.tmCreate === null
            ) {
                util.MessageBox.Show(this, "请填写所有必填的内容");
                return false;
            }

            if (util.String.isNullOrEmpty(this.credit.data)) {
                return util.MessageBox.Show(this, "必须上传凭据")
            }

            N.credit = this.credit.data;

            switch (N.type) {
                case Integers.PaymentMethod.PUBLIC_ALIPAY:
                    if (util.String.isNullOrEmpty(N.account)) {
                        return util.MessageBox.Show(this, "必须填写支付宝账户名");
                    }
                    break;
                case Integers.PaymentMethod.PUBLIC_BANK:
                    if (util.String.isNullOrEmpty(N.account) || util.String.isNullOrEmpty(N.bank)) {
                        return util.MessageBox.Show(this, "必须填写银行账户名。");
                    }
                    break;
                case Integers.PaymentMethod.PUBLIC_MONEY:
                    break;
            }

            this.onProcesssing = true;

            try {
                let r = await $.ajax("/api/charge/public/new", N);
                this.onProcesssing = false;
                if (r.code === 0) {
                    this.shouldNewChargeDialogOpen = false;
                    util.MessageBox.Show(this, "新增订单成功。");
                    this.$refs.dt.refresh();
                    return;
                }
                else {
                    util.MessageBox.Show(this, "新增订单失败。"+r.msg);
                }
            }
            catch(err) {
                this.onProcesssing = false;
                console.error(err);
                util.MessageBox.Show(this, "新增订单失败。");
            }

            return true;
        }
    }
}
</script>
