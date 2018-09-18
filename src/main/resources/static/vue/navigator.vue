<template>
    <Menu :active-name="'account'" @on-select="select" theme="dark">
        <MenuGroup title="" v-if="!isAdmin">
            <MenuItem name="account">
                <Icon type="md-document" />
                账号信息
            </MenuItem>
            <MenuItem name="orders">
                <Icon type="md-document" />
                我的订单
            </MenuItem>
            <MenuItem name="company">
                <Icon type="md-chatbubbles" />
                我的公司
            </MenuItem>
            <MenuItem name="receipt">
                <Icon type="md-chatbubbles" />
                开票服务
            </MenuItem>
            <MenuItem name="tax">
                <Icon type="md-chatbubbles" />
                税金管理
            </MenuItem>
        </MenuGroup>
        <MenuGroup v-else>
            <MenuItem name="admin">
                <Icon type="md-heart" />
                账号信息
            </MenuItem>
            <MenuItem name="customer">
                <Icon type="md-heart" />
                客户管理
            </MenuItem>
            <MenuItem name="srvpack">
                <Icon type="md-heart" />
                服务包管理
            </MenuItem>
            <MenuItem name="user" v-if="P.AdminUserListOfCurrentGroup">
                <Icon type="md-leaf" />
                用户管理
            </MenuItem>
            <MenuItem name="group" v-if="P.AdminUserListAll && P.AdminUserModifyOthers">
                <Icon type="md-leaf" />
                组管理
            </MenuItem>
            <MenuItem name="log" v-if="P.LogViewAndExport">
                <Icon type="md-leaf" />
                查看日志
            </MenuItem>
            <MenuItem name="role" v-if="P.PermissionManger">
                <Icon type="md-leaf" />
                权限管理
            </MenuItem>
            <MenuItem name="system" v-if="P.SystemConfigAdvanced">
                <Icon type="md-leaf" />
                系统配置
            </MenuItem>
        </MenuGroup>
    </Menu>
</template>
<script>
    import API from '../js/api.js';
    import util from '../js/util.js';
    import PM from '../js/permission.js';

    window.config.P = PM.forAllPermissions();

    export default {
        data () {
            return {
                active: 1,
                isAdmin: window.config.isAdmin,
                isSuperAdmin: window.config.isSuperAdmin,
                P: PM.forAllPermissions()
            }
        },
        methods: {
            select(name) {
                console.log('Route to: /' + name);
                this.$emit('menuselect', name);
            }
        },
        computed: {
            
        },
        mounted() {

        }
    }
</script>
<style>
.menu-fw {
    height: 100
}
</style>