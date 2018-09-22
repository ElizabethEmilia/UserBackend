<template>
    <div>
        <div class="logo" onclick="location.href='http://pmo076db8.hz1.17uhui.com.cn/page72'">
            <img :src="'http://pmo076db8.pic40.websiteonline.cn/upload/nmww.png'" title="增薪宝" alt="增薪宝">
        </div>
        <div class="layout" v-if="isAdmin || isCustomerPaid">
            <Sider :style="{position: 'fixed', width: '240px', minWidth: '240px', maxWidth: '240px', height: '100vh', left: 0, overflow: 'auto', overflowY: 'hidden', paddingTop: '40px'}">
                <Navigator @menuselect="menusel"/>
            </Sider>
            <Layout :style="{marginLeft: '240px'}">
                <Header :style="{background: '#fff',  boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)'}">
                    <h2 class="ib">{{ moduleName }}</h2>
                    <div class="ib" style="float: right; font-size: 13.5px;"><a href="/logout">退出登录</a></div>
                </Header>
                <Content :style="{padding: '0 16px 16px'}">
                    <div :style="{marginTop: '20px', minHeight: '420px'}">
                        <router-view></router-view>
                    </div>
                </Content>
            </Layout>
        </div>
        <div v-else>
            <RequirePayment />
        </div>

        <Notification />
    </div>

</template>

<script>
    import Navigator from './navigator.vue';
    import RequirePayment from './components/modal/requirepayment.vue';
    import ModuleConfig from '../module_config.json';
    import Notification from './components/notification.vue';
    import util from '../js/util.js';
    import API from '../js/api.js';

    const defaultModuleName = ModuleConfig.defaultModule;

    export default {
        components: {
            Navigator, RequirePayment, Notification,
        },
        data: () => ({
            moduleName: defaultModuleName,
            isAdmin: window.config.isAdmin,
            isCustomerPaid: window.config.isCustomerPaid,
        }),
        methods: {
            menusel(name) {
                this.$router.push('/'+name);
                let moduleName = ModuleConfig.displayNames[name];
                this.moduleName = moduleName ? moduleName : '';
            }
        },
        mounted() {
            let s = setInterval(async () => {
                try {
                    await API.Login.heartbeat();
                }
                catch(e) {
                    console.error(e);
                    clearInterval(s);
                    await util.MessageBox.ShowAsync(this, "您已与服务器断开连接，请重新登录");
                    location.href = "/login";
                }
            }, 30000);
        }
    }
</script>

<style>
.main-view {
    margin: 10px;
    margin-top: 50px;
}

.container {
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
}

.bg-gray {
    background-color: #eee;
}

.ib {
    display: inline-block;
}

    .logo {
        position: fixed;
        top: 0px;
        left: 0px;
        z-index: 9;
        padding-left: 45px;
        padding-top: 25px;
        cursor: pointer;
    }
</style>