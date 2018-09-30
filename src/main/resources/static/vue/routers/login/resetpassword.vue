<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 1); width: 450px; height: 270px;">
                <h2 style="text-align: center">重设密码</h2>
            <div v-if="!pending" style="margin-top: 72px;">
                
                <!--验证码 -->
                <!--VerifyCode @on-code="inputImageCode" /-->

                <div>
                    <Input type="password" class="tp" v-model="param.password" :maxlength="32" placeholder="设置新密码" style="width: 300px; margin-top: 20px;" />
                </div>

                <div style="margin-top: 35px;">
                    <Button  @click="findpassword" style="width: 300px;" type="primary" :loading="pending">重设密码</Button>
                    <br/>
                    <br/>
                    <!--a class="reg" href="javascript:void(0)" @click="show('LoginFrame')">&lt; 返回用户登录</a-->
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

import VerifyCode from '../../components/verifycode.vue';
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import md5 from 'js-md5';

export default {
    components: {
        VerifyCode
    },
    data: () => ({
        username: '',
        password: '',
        code: '',

        pending: false,
        param: {
            uid: '',
            token: '',
            password: '',
        },
        
        sent: false,
        pending: false,
        imageCode: '',

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
        
        // 从组件获取图片验证码
        inputImageCode(val) {
            this.imageCode = val;
            console.log(this.imageCode);
        },

        // 找回密码
        async findpassword() {
            if (this.pending)
                return;

            if (util.isStringNullOrEmpty(this.param.password)) {
                util.MessageBox.Show(this, '请设置密码。');
                return;
            }

            if (!util.passwordMatchesRestriction(this.param.password)) {
                util.MessageBox.Show(this, '密码不符合复杂度要求。密码需要包含大写字母，小写字母，数字或符号的至少两种，且长度在8-16位之间。');
                return;
            }

            this.param.password = md5(this.param.password);
            this.pending = true;

            try {
                let result = await $.ajax('/api/resetpwd', this.param);
                if (result.code === 0) {
                    await util.MessageBox.ShowAsync(this, '重设密码成功，现在可以使用新密码登录了。');
                    location.href = "./";
                }
                else {
                    util.MessageBox.Show(this, result.msg?result.msg:'无法连接到服务器');
                    throw Error(result);
                }
            }
            catch (err) {
                console.error(err);
                this.pending = false;
                util.MessageBox.Show(this, "请求已被服务器拒绝。");
            }
        }
    },
    watch: {

    },
    created() {
        // 验证页面是否正确
        let uid = util.getQueryParameter('uid');
        let token = util.getQueryParameter('token');
        if (util.isStringNullOrEmpty(uid) || util.isStringNullOrEmpty(token)) {
            alert('非法请求。');
            location.href="/login";
            return;
        }
        this.param.uid = uid;
        this.param.token = token;
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

