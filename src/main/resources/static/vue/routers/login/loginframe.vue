<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 1); width: 450px; height: 300px;">
                <h2 style="text-align: center">登录系统</h2>
            <div v-show="!pending">
                <div>
                    <Input class="tp" :readonly="pending" v-model="username" prefix="md-contact" autofocus :maxlength="16" placeholder="用户名或手机号码" style="background: rgba(255, 255, 255, 0.75);border-color: rgba(255, 255, 255, 0.8); width: 300px; margin-top: 60px;   border-radius: 15px;" />
                </div>

                <!--验证码 -->
                <!--VerifyCode @on-code="inputImageCode" prefix="md-code" /-->

                <div>
                    <Input type="password" :readonly="pending" v-model="password" @on-1="login()" class="tp" prefix="md-lock"  :maxlength="32" placeholder="密码" style="background: rgba(255, 255, 255, 0.75);border-color: rgba(255, 255, 255, 0.8); width: 300px; margin-top: 20px; border-radius: 15px;" />
                </div>

                <!--新版验证码 -->
                <VerifyCodeSlider
                        ref="verfcode"
                        @on-verifyok="verifyOKHandler"
                        style="margin-left: 60px;margin-top: 20px;"/>

                <div>
                    <Button @click="login()" type="success" style="background: #f60;border-color: #f60; width: 300px; margin-top: 60px; font-size: 14px;">登录</Button>
                </div>

                <div style="margin-top: 15px;">
                    <a class="reg" href="javascript:void(0)" @click="show('RegisterFrame')">注册新用户</a> 
                    <a class="reg" style="margin-left: 10px;" href="javascript:void(0)" @click="show('ForgetPassword')">忘记密码</a>
                </div>
            </div>

            <div v-show="pending" style="margin-top: 90px;">
                <Spin fix size="large">
                    <p style="font-size: 13px; margin-top: 10px;">
                        <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
                        <div>正在登录...</div>
                    </p>
                </Spin>

            </div>
    </Card>
</template>

<script>
// 事件
//   on-request-change-com (name)  - 请求转换组件时触发
//   on-password (MD5 of password) - 输入密码时触发
//   on-username (Username)        - 用户名

import VerifyCode from '../../components/verifycode.vue';
import VerifyCodeSlider from '../../components/trackverfcode.vue';
import util from '../../../js/util.js';
import $ from '../../../js/ajax.js';
import md5 from 'js-md5';

export default {
    components: {
        VerifyCode, VerifyCodeSlider,
    },
    data: () => ({
        username: '',
        password: '',
        code: '',

        pending: false,
        verifyOk: false,
    }),
    methods: {
        async login() {
            if (util.isStringNullOrEmpty(this.username) || util.isStringNullOrEmpty(this.password)) {
                util.MessageBox.Show(this, '请输入用户名和密码');
                return;
            }

            if (!this.verifyOk) {
                util.MessageBox.Show(this, '请拖动滑块拼图进行验证');
                return;
            }

            let password = md5(this.password);

            if (this.pending)
                return;
            this.pending = true;
            
            try {
                let result = await $.ajax('/api/login', {
                    name: this.username,
                    password,
                    code: this.code,
                });
                if (result.code === 0) {
                    //await util.MessageBox.ShowAsync(this, '登陆成功');
                    location.href = "./";
                }
                else {
                    this.$refs.verfcode.reload();
                    this.verifyOk = false;
                    await util.MessageBox.ShowAsync(this, '' + result.msg);
                    this.pending = false;
                }
            }
            catch(err) {
                this.$refs.verfcode.reload();
                this.verifyOk = false;
                await util.MessageBox.ShowAsync(this, '无法连接服务器');
                this.pending = false;
            }
        },
        show(name) {
            this.$emit('on-request-change-com', name);
        },
        // 从组件获取图片验证码
        inputImageCode(val) {
            this.code = val;
        },
        verifyOKHandler() {
            this.verifyOk = true;
            // 如果输完密码和用户名了  直接登录，不需要用户点登录按钮
            if (!util.isStringNullOrEmpty(this.username) && !util.isStringNullOrEmpty(this.password)) {
                this.login();
            }
        }
    },
    watch: {
        username(val) {
            this.$emit('on-username', val);
        },
        password(val) {
            this.$emit('on-password', val);
        },
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

.demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
}

    .ivu-input {
        border-radius: 15px;
    }
</style>

