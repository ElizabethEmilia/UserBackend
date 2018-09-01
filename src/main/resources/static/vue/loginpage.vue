<template>
    <div class="main">
        <Row type="flex" justify="center" align="middle" class="main code-row-bg">
            <!-- Component -->
            <div v-if="current == 'LoginFrame'" >
                <LoginFrame @on-request-change-com="changeComp" @on-username="inputUser" @on-password="inputPassword" />
            </div>
            <div v-else-if="current == 'RegisterFrame'">
                <RegisterFrame @on-request-change-com="changeComp" />
            </div>
            <div v-else-if="current == 'ForgetPassword'">
                <ForgetPassword @on-request-change-com="changeComp"  />
            </div>
            <div v-else-if="current == 'ResetPassword'">
                <ResetPassword @on-request-change-com="changeComp" />
            </div>
        </Row>
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
        this.current = window.findResult ? 'ResetPassword' : this.current;
    }
}
</script>

<style>

</style>
