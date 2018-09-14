<template>
    <div style="right: 0; margin: 0 auto; position: absolute; top: 165px; height: 600px; background: #fff; ">
        <div >
            <!-- Component -->
            <div v-show="current == 'LoginFrame'" class="border" style="margin-top: 40px">
                <LoginFrame @on-request-change-com="changeComp" @on-username="inputUser" @on-password="inputPassword" />
            </div>
            <div v-if="current == 'RegisterFrame'" class="border">
                <RegisterFrame @on-request-change-com="changeComp" />
            </div>
            <div v-else-if="current == 'ForgetPassword'" class="border">
                <ForgetPassword @on-request-change-com="changeComp"  />
            </div>
            <div v-else-if="current == 'ResetPassword'" class="border">
                <ResetPassword @on-request-change-com="changeComp" />
            </div>
        </div>
    </div>
</template>

<script>
import LoginFrame from './routers/login/loginframe.vue';
import RegisterFrame from './routers/login/registerframe.vue';
import ForgetPassword from './routers/login/forgetpassword.vue';
import ResetPassword from './routers/login/resetpassword.vue';

export default {
    components: {
        LoginFrame, RegisterFrame, ForgetPassword, ResetPassword
    },
    data: () => ({
        current: 'LoginFrame',
        user:  '',
        password: '',
    }),
    methods: {
        changeComp(name) {
            this.current = name;
        },
        inputUser(user) {
            this.user = user;
        },
        inputPassword(password) {
            this.password = password;
        }
    },
    created() {
        this.current = location.pathname === "/reset" ? 'ResetPassword' : this.current;
    }
}
</script>

<style>
    .border {

    }
</style>
