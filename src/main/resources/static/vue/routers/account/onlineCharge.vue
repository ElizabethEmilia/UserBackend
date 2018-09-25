<template>
    <!-- 在线充值 -->
        <Card class="card-margin">
            <Divider orientation="left"><h3>在线充值</h3></Divider>
            
            <Alert type="success"><span style="font-weight: blod; color: green">温馨提示：</span> 微信、支付宝充值即时到账。</Alert>

            <div style="margin-top: 20px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    充值金额
                </span> 
                <Poptip trigger="focus">
                    <InputNumber :step="100" placeholder="" v-model="onlinePayAmount" style="width: 200px; font-size: 14px" />                    
                    <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber }}</span></div>
                </Poptip>
                <p>
                    <span style="font-size: 16px; width: 100px; display:inline-block"></span>
                    * 请输入100到1,000,000之间的整数。
                </p>
                
            </div>

            <!--div style="margin-top: 5px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    支付方式
                </span> 
                <RadioGroup v-model="onlinePayMethod" type="button">
                    <Radio label="微信支付" :value="0"></Radio>
                    <Radio label="支付宝" :value="1"></Radio>
                </RadioGroup>
                
            </div-->

            <div style="margin-top: 5px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    充值类型
                </span>
                <Select v-model="dst" style="width: 100px;">
                    <Option label="年费" :value="0"></Option>
                    <Option label="税金" :value="1"></Option>
                    <Option label="其他" :value="2"></Option>
                </Select>

            </div>

            <div style="margin-top: 5px; margin-left: 30px;">
                <Checkbox v-model="onlinePayReadTerms">我已阅读<a href="javascript:void(0)" @click="readTerms()">《服务条款》</a></Checkbox>。
                
            </div>

            <div class="inline-margin">
                <Button type="primary"  style="width: 200px;" shape="circle" icon="ios-card" :disabled="!onlinePayReadTerms" @click="onlinePaymentCharge">充值</Button>
            </div>

            <Modal v-model="confirmDialogVisible" fullscreen >
                <div slot="footer">
                </div>

                <ConfirmOrders
                        @on-start-pay="val => {confirmDialogVisible = false; confirmResultDialog=true; payMethod = val;}"
                        @on-cancel="confirmDialogVisible = false;"
                        :dst="dst"
                        :param="{ amount: chargeAmount }" />

            </Modal>

            <Modal
                    :closable="false"
                    :mask-closable="false"
                    v-model="confirmResultDialog">
                <ConfirmResult
                        :method="payMethod"
                        @on-cancel="handleCancel"
                />

                <div slot="footer">
                </div>

            </Modal>

        </Card>

</template>

<script>

import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';
import util from '../../../js/util.js';
import ServiceTermsDialog from './dialogs/serviceterms.vue';
import API from '../../../js/api.js';
import ConfirmOrders from '../../routers/orders/confirmorders.vue';
import ConfirmResult from '../../components/confirmchargeresult.vue';

export default {
    components: {
        ConfirmOrders, ConfirmResult,
    },
    data: () => ({
        onlinePayAmount: 0,
        onlinePayReadTerms: false,

        confirmDialogVisible: false,
        confirmResultDialog: false,
        payMethod: '',

        dst: 0,

        chargeAmount: 0,
    }),
    methods: {
        // 在线充值
        async onlinePaymentCharge() {
            //console.log(Vue, $);
            let amount = Number(this.onlinePayAmount);
            if (this.onlinePayAmount === "" || Number.isNaN(amount))
                return util.MessageBox.Show(this, '请输入数字');
            // 检查是不是整数
            if (Number.parseInt(amount) !== amount)
                return util.MessageBox.Show(this, '请输入整数');

            // 检查数字大小
            if (100 > amount)
                return util.MessageBox.Show(this, '最少充值100元');
            if (amount > 1000000)
                return util.MessageBox.Show(this, '最多充值1,000,000元');

            this.chargeAmount = amount;
            this.confirmDialogVisible = true;
            // 充值种类
            //let type = ({ "微信支付": "wechat", "支付宝": "alipay" })[this.onlinePayMethod];

            //try {
            //    let result = await $.ajax('/api/charge/' + type, { amount });
            //    if (result.code) {
            //        return util.MessageBox.Show(this, '充值失败。' + result.msg);
            //    }
            //    util.MessageBox.Show(this, '暂时不知道充值陈宫需要其他的什么步骤  但肯定不是这样就能成功的   肯定还需要其他的步骤');
            //}
            //catch(err) {
            //    util.MessageBox.Show(this, '无法连接服务器');
            //}
        },
        // 阅读服务协议
        readTerms() {
            this.$Modal.info({
                title: '服务条款',
                width: 800,
                render: h => h(ServiceTermsDialog, {
                    props: {
                        content: 'terms'
                    }
                 })
            })
        },

        handleCancel() {
            location.href = "./";
        }
    },
    computed: {
        formatNumber () {
            if (this.onlinePayAmount === '') return '0';
            function parseNumber(str) {
                return str.split(/(?=(\d{3})+$)/g).filter((e,i)=>i%2==0).join(',');
            }
            let [str, prec] = (''+this.onlinePayAmount).split('.');
            return parseNumber(str) + (prec ? '.'+prec : '');
        }
    }
}
</script>
