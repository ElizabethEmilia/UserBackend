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
            <div v-else-if="current == 'FindPasswordFrame'">

            </div>
            
        </Row>
    </div>
</template>

<script>
import LoginFrame from './routers/login/loginframe.vue';
import RegisterFrame from './routers/login/registerframe.vue';

export default {
    components: {
        LoginFrame, RegisterFrame
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
    }
}
</script>

<style>

</style>
