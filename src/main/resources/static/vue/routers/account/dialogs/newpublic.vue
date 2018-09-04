<template>
    <Row style="margin-top: 10px">
        <Col span="12">
            <Tabs type="card" :value="paymentMethod" :height="500">
                <TabPane name="bank" label="银行支付">
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />客户名称 </span>
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
                <TabPane name="cash" label="现金支付"></TabPane>
                <TabPane name="alipay" label="支付宝支付"></TabPane>
            </Tabs>
        </Col>
        <Col span="12">
            <UploadFile title="上传支付凭证"/>
        </Col>
    </Row>
</template>

<script>
import '../../../../css/style.less';
import MoneyInput from '../../../components/moneyinput.vue';
import UploadFile from '../../../components/uploadfile.vue';

export default {
    components: {
        MoneyInput, UploadFile,
    },
    data: ()=>({
        paymentMethod: 'bank',
        inputInit:  {
            "type": "",
            "amount": "",
            "name": "",
            "account": "",
            "bank": "",
            "credit": "",
            "tmCreate": "",
        },
        bankInput: null,
        cashInput: null,
        aliapyInput: null,
        amount: 0,
    }),
    mounted() {
        this.bankInput = Object({}, this.inputInit);
        this.cashInput = Object({}, this.inputInit);
        this.aliapyInput = Object({}, this.inputInit);
    }
}
</script>

<style>
.dm {
    margin-top: 10px;
}
</style>

