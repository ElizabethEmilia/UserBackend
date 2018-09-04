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

            <Tabs :value="req_url" @on-click="tabclick">
                <TabPane label="全部" name="all"></TabPane>
                <TabPane label="已确认" name="confirmed"></TabPane>
                <TabPane label="待确认" name="pending"></TabPane>
                <TabPane label="已取消" name="cancelled"></TabPane>
            </Tabs>

            <ButtonGroup style="margin: 15px;">
                <Button @click="newOrder">新增订单</Button>
                <Button>取消订单</Button>
                <Button>查看详情</Button>
            </ButtonGroup>

            <PagedTable :columns="publicTransferColumnName" :data-source="dataSource + req_url" />
    </Card>
</template>

<script>

import PagedTable from '../../pagedTable.vue';
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import PublicChargeDialog from './dialogs/newpublic.vue';

export default {
    components: {
        PagedTable
    },
    data: () => ({
        publicBankAccount: [ // 从配置文件获取
            {
                recommend: true,
                accountName: 'xxxx公司',
                bankName: 'xxxx银行',
                accountNo: '10030010000'
            },
            {
                recommend: false,
                accountName: 'xxxx公司',
                bankName: 'xxxxxx银行',
                accountNo: '20030010000'
            },
        ],
        publicTransferColumnName() {
            let self = this;
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: '序号', type: 'index', width: 80, },
                { title: '订单编号', key: 'id' },
                { title: '订单类型', render: (h, params) =>  h('span', {}, paymentMethod[this.tableData[params.index].type]) },
                { title: '订单金额', key: 'amount' },
                { title: '订单状态', render: (h, params) => h('span', {}, publicOrderStatus[this.tableData[params.index].status]) },
                { title: '下单时间', key: 'tmCreate' },
                { title: '确认时间', key: 'tmConfirm' },
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
                                                util.MessageBox.Show(this, '操作失败');
                                            }
                                            else {
                                                util.MessageBox.Success(this, '操作成功');
                                            }
                                        }
                                        catch(err) {
                                            util.MessageBox.Success(this, '操作失败');
                                        }
                                    }
                                }
                            }, '取消订单'),
                            h('span', {}, ' | '),
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    async click() {
                                        let info = self.tableData[params.index];
                                        /// 弹出对话框
                                    }
                                }
                            }, '查看详情')
                        ]);
                    }
                }
            ]
        },

        dataSource: "publiccharge/",
        req_url: "all",

        newConfig: {

        },
    }),

    methods: {
        tabclick(name) {
            this.res_url = name;
        },
        async newOrder() {
            try {
                await util.MessageBox.ShowComponentAsync(this, PublicChargeDialog, "新增订单", {
                    
                }, { width: 800, });
                alert('用户提交了操作');
            }
            catch(err) {
                alert('用户取消了操作');
            }
        }
    }
}
</script>
