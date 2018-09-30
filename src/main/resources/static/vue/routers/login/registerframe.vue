<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 1); width: 450px; height: 500px;">
                <h2 style="text-align: center">注册</h2>
                
                <div>
                    <Input class="tp" v-model="customer.name" autofocus :maxlength="16" placeholder="客户姓名" style="width: 300px; margin-top: 30px;" />
                </div>
                <div>
                    <Input type="password" class="tp" v-model="customer.password" :maxlength="32" placeholder="密码" style="width: 300px; margin-top: 20px;" />
                </div>

                <!--验证码 -->
                <VerifyCode @on-code="inputImageCode" />

                <!--短信验证码 -->
                <SendTextMessage 
                @on-invalid-number="handleInvalidNumber" 
                    @on-send-msg="sendMessage" 
                    @on-sent="sendMessageSuccess" 
                    @on-send-failed="sendMessageFailed" 
                    @on-input-msg-code="inputMessageCode"
                    :verify-code="imageCode" />

                <div style="width: 300px; margin: 0 auto; margin-top: 20px;">
                    <Select placeholder="所属行业" v-model="customer.industry">
                        <Option v-for="(e, i) in industry" :key="i" :value="i">{{e}}</Option>
                    </Select>
                </div>

                <div style="width: 300px; margin: 0 auto; margin-top: 20px;">
                    <Select placeholder="会员类别" v-model="customer.type">
                        <Option v-for="(e, i) in memberType" :key="i" :value="i">{{e}}</Option>
                    </Select>
                </div>

                <div style="margin-top: 35px;">
                    <Button  @click="register" style="width: 300px;" type="primary" :loading="pendingRegister">注册</Button>
                    <br/>
                    <br/>
                    <a class="reg" href="javascript:void(0)" @click="show('LoginFrame')">&lt; 返回用户登录</a>
                </div>
    </Card>
</template>

<script>
// 事件
//   on-request-change-com (name)  - 请求转换组件时触发
//   on-password (MD5 of password) - 输入密码时触发
//   on-username (Username)        - 用户名

import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import util from '../../../js/util.js';
import VerifyCode from '../../components/verifycode.vue';
import SendTextMessage from '../../components/sendtextmsg.vue';
import $ from '../../../js/ajax.js';
import md5 from 'js-md5';

export default {
    components: {
        VerifyCode, SendTextMessage
    },
    data: () => ({
        customer: {
            name: '',
            password: '', //加密以后的密码
            phone: '',
            msgcode: '',
            industry: -1,
            type: -1,
        },

        imageCode: '',
        password: '',
        industry,
        memberType,
        phone: '', // 发送短信前的手机号
        msgsent: false, // 短信是否已经发送
        pendingRegister: false
    }),
    methods: {
        // 从组件获取图片验证码
        inputImageCode(val) {
            this.imageCode = val;
            console.log(this.imageCode);
        },
        // 从组件获取短信验证码
        inputMessageCode(val) {
            this.customer.msgcode = val;
            console.log(this.customer.msgcode);
        },
        // 注册
        async register() {
            if (this.pendingRegister) {
                return;
            }

            let matchesRestriction = 
                !util.isStringNullOrEmpty(this.customer.password) &&
                !util.isStringNullOrEmpty(this.customer.name) &&
                !util.isStringNullOrEmpty(this.customer.phone) &&
                !util.isStringNullOrEmpty(this.customer.msgcode) &&
                (this.customer.industry > -1 && this.customer.industry < industry.length) &&
                (this.customer.type > -1 && this.customer.type < memberType.length);
            
            if (!matchesRestriction) {
                util.MessageBox.Show(this, '请检查你的输入是否正确');
                return;
            }

            matchesRestriction = util.passwordMatchesRestriction(this.customer.password);
            if (!matchesRestriction) {
                util.MessageBox.Show(this, '密码不符合复杂度要求。密码需要包含大写字母，小写字母，数字或符号的至少两种，且长度在8-16位之间。');
                return;
            }

            if (!this.msgsent) {
                util.MessageBox.Show(this, '请发送短信验证码');
                return;
            }

            this.pendingRegister = true;

            try {
                let result = await $.ajax('/api/register', Object.assign(this.customer, { password: md5(this.customer.password) }));
                this.pendingRegister = false;
                if (result.code === 0) {
                    util.MessageBox.Show(this, '注册成功');
                    this.show('LoginFrame');
                }
                else {
                    util.MessageBox.Show(this, '' + result.msg);
                }
            }
            catch(err) {
                console.error(err);
                util.MessageBox.Show(this, '注册失败');
                this.pendingRegister = false;
            }
        },
        show(name) {
            this.$emit('on-request-change-com', name);
        },
        sendMessageFailed(err) {
            util.MessageBox.Show(this,  err.msg ? err.msg : '发送短信失败。');
        },
        sendMessageSuccess(result) {
            this.msgsent = true;
            //util.MessageBox.Show(this, '发送短信成功');
        },
        sendMessage(phone) {
            this.customer.phone = phone;
            console.log('phone number: ' + phone);
        },
        handleInvalidNumber(phone) {
            util.MessageBox.Show(this, `${phone} 不是一个正确的手机号码。`);
        }
    },
    watch: {
        
    }
}
</script>

<style>
.main {
    position: fixed;
    top: 0px;
    left: 0px;
    height: 100%;
    width: 100%;
}

.tp {
    background: rgba(255, 255, 255, 1);
    border-color: rgba(255, 255, 255, 1);
}

a.reg {
    margin-top: 35px;
    color: gray;
    font-size: 14px;
}
a.reg:hover {
    border-bottom: 1px solid gray;
}
</style>

