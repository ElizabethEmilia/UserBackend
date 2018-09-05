<template>
    <Row style="margin-top: 10px">
        <Col span="12">
            <Tabs type="card" :value="paymentMethod" :height="500">
                <TabPane name="bank" label="银行支付">
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />支付金额 </span>
                        <MoneyInput v-model="bankInput.amount" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款方名称 </span>
                        <Input v-model="bankInput.name" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款方账号 </span>
                        <Input v-model="bankInput.account" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款银行 </span>
                        <Input v-model="bankInput.bank" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款时间 </span>
                        <DatePicker v-model="bankInput.tmCreate" clearable style="width: 200px" />
                    </div>
                </TabPane>
                <TabPane name="cash" label="现金支付">
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />支付金额 </span>
                        <MoneyInput v-model="cashInput.amount" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款方名称 </span>
                        <Input v-model="cashInput.name" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款时间 </span>
                        <DatePicker v-model="cashInput.tmCreate" clearable style="width: 200px" />
                    </div>
                </TabPane>
                <TabPane name="alipay" label="支付宝支付">
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />支付金额 </span>
                        <MoneyInput v-model="aliapyInput.amount" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款方名称 </span>
                        <Input v-model="aliapyInput.name" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />支付宝账号 </span>
                        <Input v-model="aliapyInput.account" placeholder="" clearable style="width: 200px" />
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />付款时间 </span>
                        <DatePicker v-model="aliapyInput.tmCreate" clearable style="width: 200px" />
                    </div>
                </TabPane>
            </Tabs>
        </Col>
        <Col span="12">
            <UploadFile v-model="credit" title="上传支付凭证"/>
        </Col>
    </Row>
</template>

<script>
import '../../../../css/style.less';
import MoneyInput from '../../../components/moneyinput.vue';
import UploadFile from '../../../components/uploadfile.vue';
import { Integers } from '../../../../constant.js'

 const inputInit =  {
            "type": 0,
            "amount": "",
            "name": "",
            "account": "",
            "bank": "",
            "credit": "",
            "tmCreate": "",
        };

export default {
    components: {
        MoneyInput, UploadFile,
    },
    data: ()=>({
        paymentMethod: 'bank',
        inputInit:  {
            "type": 0,
            "amount": "",
            "name": "",
            "account": "",
            "bank": "",
            "credit": "",
            "tmCreate": "",
        },
        bankInput: inputInit,
        cashInput: inputInit,
        aliapyInput: inputInit,
        amount: 0,
        credit: null,
    }),
    mounted() {
        this.bankInput.type = Integers.PaymentMethod.PUBLIC_BANK;
        this.cashInput.type = Integers.PaymentMethod.PUBLIC_MONEY;
        this.aliapyInput.type = Integers.PaymentMethod.PUBLIC_ALIPAY;
    },
    watch: {
        bankInput: {
            handler(val) { return this.$emit('on-input', val); },
            deep: true,
        },
        alipayInput: {
            handler(val) { return this.$emit('on-input', val); },
            deep: true,
        },
        cashInput: {
            handler(val) { console.log(val); return this.$emit('on-input', val); },
            deep: true,
        },
        credit(val) {
            return this.$emit('on-selectcredit', val);
        }
    }
}
</script>

<style>
.dm {
    margin-top: 10px;
}
</style>

