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
                    <InputNumber :step="100" size="middle" placeholder="" v-model="onlinePayAmount" style="width: 200px; font-size: 14px" />                    
                    <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber }}</span></div>
                </Poptip>
                <p>
                    <span style="font-size: 16px; width: 100px; display:inline-block"></span>
                    * 请输入100到1,000,000之间的整数。
                </p>
                
            </div>

            <div style="margin-top: 5px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    支付方式
                </span> 
                <RadioGroup v-model="onlinePayMethod" type="button">
                    <Radio label="微信支付" value="0"></Radio>
                    <Radio label="支付宝" value="1"></Radio>
                </RadioGroup>
                
                
            </div>

            <div style="margin-top: 5px; margin-left: 30px;">
                <Checkbox v-model="onlinePayReadTerms">我已阅读<a href="javascript:void(0)">《服务条款》</a></Checkbox>。
                
            </div>

            <div class="inline-margin">
                <Button type="primary" size="middle" style="width: 200px;" shape="circle" icon="ios-card" :disabled="!onlinePayReadTerms" @click="onlinePaymentCharge">充值</Button>
            </div>
        </Card>

</template>

<script>

import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';

export default {
    data: () => ({
        onlinePayMethod: 0,
        onlinePayAmount: "", // :String
        onlinePayReadTerms: false,
    }),
    methods: {
        // 在线充值
        onlinePaymentCharge() {
            //console.log(Vue, $);
            let amount = Number(this.onlinePayAmount);
            if (this.onlinePayAmount === "" || Number.isNaN(amount)) {
                alert('请输入数字');
                return;
            }
            alert('/// TODO: Onlien charge')
        },
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
