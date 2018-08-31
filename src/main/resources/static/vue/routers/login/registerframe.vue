<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 0.4); width: 450px; height: 450px;">
                <h2 style="text-align: center">注册</h2>
                
                <div>
                    <Input class="tp" v-model="customer.name" autofocus maxlength="16" placeholder="客户姓名" style="width: 300px; margin-top: 30px;" />
                </div>
                <div>
                    <Input type="password" class="tp" v-model="customer.password" maxlength="32" placeholder="密码" style="width: 300px; margin-top: 20px;" />
                </div>
                <div style="width: 300px; margin: 0 auto; margin-top: 20px;">
                    <Row>
                        <Col span="15">
                            <Input class="tp"  maxlength="32" v-model="customer.phone" placeholder="手机号码" style="" />
                        </Col>
                        <Col span="6">
                            <Button style="width: 105px; margin-left: 5px;" 
                                :disabled="sendMessageButtonTimeCountdown != 0" 
                                @click="sendMessageCode"
                                v-html=" sendMessageButtonTimeCountdown == 0 ? sendMessageText : '' +  sendMessageButtonTimeCountdown + '秒后重发'">
                                Send Message
                            </Button>
                        </Col>
                    </Row>
                   
                </div>

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

export default {
    data: () => ({
        customer: {
            name: '',
            password: '', //加密以后的密码
            phone: '',
            msgcode: '',
            industry: -1,
            type: -1,
        },
        password: '',
        sendMessageText: '发送验证码',
        sendMessageButtonTimeCountdown: 0,
        industry,
        memberType,

        pendingRegister: false
    }),
    methods: {
        getCode() {
            // 获取验证码
            alert('TODO:/// Login!!');
        },
        // 注册
        register() {
            this.pendingRegister = true;
            alert('注册');
        },
        show(name) {
            this.$emit('on-request-change-com', name);
        },
        // 发送短信
        sendMessageCode() {
            alert('已发送短信。请等待查收');
            this.startCountDown();
        },
        // 开始倒计时
        startCountDown() {
            this.sendMessageButtonTimeCountdown = 60;
            let interval = setInterval(() => {
                if (this.sendMessageButtonTimeCountdown <= 0) {
                    clearInterval(interval);
                    return;
                }
                this.sendMessageButtonTimeCountdown--;
            }, 1000);
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
    background: rgba(255, 255, 255, 0.75);
    border-color: rgba(255, 255, 255, 0.8);
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

