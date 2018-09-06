<template>
    <div class="layout">
        <Layout>
            <Header>
                <Menu mode="horizontal" theme="dark" active-name="1">
                    <div class="layout-logo"></div>
                    <div class="layout-nav">
                        <MenuItem name="1">
                            <Icon type="ios-navigate1"></Icon>

                        </MenuItem>
                        <MenuItem name="2">
                            <Icon type="ios-keypad1"></Icon>

                        </MenuItem>
                        <MenuItem name="3">
                            <Icon type="ios-analytics1"></Icon>

                        </MenuItem>
                        <MenuItem name="4">
                            <Icon type="ios-paper"></Icon>
                            退出登录
                        </MenuItem>
                    </div>
                </Menu>
            </Header>
            <Layout :style="{padding: '0 50px'}">
                <Content :style="{padding: '24px 0', minHeight: '100vh', background: '#fff'}">
                    <Layout>
                        <Sider hide-trigger :style="{background: '#fff', minHeight: '100vh', width: '240px', minWidth: '240px', maxWidth: '240px'}">
                            <Affix>
                                <Navigator @menuselect="menusel"/>
                            </Affix>
                        </Sider>
                        <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">
                            <router-view></router-view>
                        </Content>
                    </Layout>
                </Content>
            </Layout>
            <Footer class="layout-footer-center">2018 &copy; </Footer>
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

.layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
}
.layout-logo{
    width: 100px;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
}
.layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
}
.layout-footer-center{
    text-align: center;
}
</style>