<template>
    <div class="layout">
        <Sider :style="{position: 'fixed', width: '240px', minWidth: '240px', maxWidth: '240px', height: '100vh', left: 0, overflow: 'auto', overflowY: 'hidden'}">
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
</template>

<script>
    import Navigator from './navigator.vue';
    import ModuleConfig from '../module_config.json';

    const defaultModuleName = ModuleConfig.defaultModule;

    export default {
        components: {
            Navigator
        },
        data: () => ({
            moduleName: defaultModuleName
        }),
        methods: {
            menusel(name) {
                this.$router.push('/'+name);
                let moduleName = ModuleConfig.displayNames[name];
                this.moduleName = moduleName ? moduleName : '';
            }
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
</style>