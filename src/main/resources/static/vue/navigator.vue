<template>
    <Menu :active-name="account" @on-select="select" theme="dark">
        <MenuGroup title="">
            <MenuItem name="account">
                <Icon type="md-document" />
                账号信息
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
        <MenuGroup title="系统管理">
            <MenuItem name="customer">
                <Icon type="md-heart" />
                客户管理
            </MenuItem>
            <MenuItem name="user">
                <Icon type="md-leaf" />
                用户管理
            </MenuItem>
            <MenuItem name="log">
                <Icon type="md-leaf" />
                查看日志
            </MenuItem>
        </MenuGroup>
    </Menu>
</template>
<script>
    export default {
        data () {
            return {
                active: 1
            }
        },
        methods: {
            select(name) {
                console.log('Route to: /' + name);
                this.$emit('menuselect', name);
            }
        },
        computed: {
            
        }
    }
</script>
<style>
.menu-fw {
    height: 100
}
</style>