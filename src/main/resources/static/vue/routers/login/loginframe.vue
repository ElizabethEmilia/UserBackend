<template>
    <Card dis-hover :bordered="false" style="text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 0.4); width: 450px; height: 300px;">
                <h2 style="text-align: center">登录系统</h2>
            <div v-if="!pending">
                <div>
                    <Input class="tp" :readonly="pending" v-model="username" prefix="md-contact" autofocus maxlength="16" placeholder="用户名" style="background: rgba(255, 255, 255, 0.75);border-color: rgba(255, 255, 255, 0.8); width: 300px; margin-top: 100px;" />
                </div>
                <div>
                    <Input type="password" :readonly="pending" v-model="password" @on-click="getCode()" class="tp" prefix="md-lock"  maxlength="32" placeholder="密码" :icon="pending ? 'ios-loading' : 'md-arrow-forward'" style="background: rgba(255, 255, 255, 0.75);border-color: rgba(255, 255, 255, 0.8); width: 300px; margin-top: 20px;" />
                </div>
            
                <div style="margin-top: 35px;">
                    <a class="reg" href="javascript:void(0)" @click="show('RegisterFrame')">注册新用户</a>
                </div>
            </div>

            <div v-else style="margin-top: 90px;">
                正在登录... 
            </div>
    </Card>
</template>

<script>
// 事件
//   on-request-change-com (name)  - 请求转换组件时触发
//   on-password (MD5 of password) - 输入密码时触发
//   on-username (Username)        - 用户名



export default {
    data: () => ({
        username: '',
        password: '',
        code: '',

        pending: false
    }),
    methods: {
        getCode() {
            if (this.pending)
                return;
            this.pending = true;
            // 获取验证码
            alert('TODO:/// Login!!');
        },
        show(name) {
            this.$emit('on-request-change-com', name);
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

