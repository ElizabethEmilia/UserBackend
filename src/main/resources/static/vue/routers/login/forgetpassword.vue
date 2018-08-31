<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 0.4); width: 450px; height: 300px;">
                <h2 style="text-align: center">找回密码</h2>
            <div v-if="!pending">
                
                <SendTextMessage />

                <div style="margin-top: 35px;">
                    <Button  @click="register" style="width: 300px;" type="primary" :loading="pendingRegister">找回密码</Button>
                    <br/>
                    <br/>
                    <a class="reg" href="javascript:void(0)" @click="show('LoginFrame')">&lt; 返回用户登录</a>
                </div>
            </div>

            <div v-else style="margin-top: 90px;">
                正在验证
            </div>
    </Card>
</template>

<script>
// 事件
//   on-request-change-com (name)  - 请求转换组件时触发
//   on-password (MD5 of password) - 输入密码时触发
//   on-username (Username)        - 用户名

import SendTextMessage from '../../components/sendtextmsg.vue';

export default {
    components: {
        SendTextMessage
    },
    data: () => ({
        username: '',
        password: '',
        code: '',

        pending: false,
        param: {
            phone: '',
            msgcode: '',
        },
        
    }),
    methods: {
        getCode() {
            if (this.pending)
                return;
            this.pending = true;
            // 获取验证码
            alert('TODO:/// msgcode!!');
        },
        show(name) {
            this.$emit('on-request-change-com', name);
        },
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

