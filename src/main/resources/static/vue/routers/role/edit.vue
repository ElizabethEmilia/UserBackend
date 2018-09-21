<template>
    <div>
        <div style="font-size: 13.5px;">

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />角色名称 </span>
                <Input v-model="roleEdit.name" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> 角色描述 </span>
                <Input v-model="roleEdit.remarks" placeholder="" clearable style="width: 200px" />
            </div>

            <h2>角色权限</h2>

            权限值： {{ roleEdit.value }}

            <div style="margin-bottom: 5px;" v-for="group in moudles">
                <h3>{{ group.group }}</h3>
                <div style="margin-bottom: 5px;" v-for="m in group.submodules">
                    <span class="title-before-input"></span>
                    <Checkbox
                            style="font-size: 13.5px"
                            :value="(roleEdit.value & (1 << m.value)) > 0"
                            @on-change="val => val ? (roleEdit.value |= (1 << m.value)) : (roleEdit.value &= ~(1 << m.value))"
                    >{{ m.name }}</Checkbox>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    /*
    参数：
         edit： true/false
         value: object { name, value, id, remark }
     */
    import md5 from 'js-md5';
    import init from '../../../js/init.js';

    const initRole = init.tRole;

    export default {
        props: [
            'role', 'value', 'edit',
        ],
        data: () => ({
            roleEdit: init.tRole,
            moudles: [
                {
                    group: "客户管理",
                    mask: 0,
                    submodules: [
                        { name: "查看当前用户名下的客户信息", value: 3 },
                        { name: "查看当前组的所有客户信息", value: 4 },
                        { name: "查看所有客户信息", value: 5 },
                        { name: "增加新的客户", value: 6 },
                        { name: "修改客户信息", value: 7 },
                        { name: "删除客户信息", value: 8 },
                        { name: "增加或修改客户的公司信息", value: 9 },
                        { name: "删除客户的公司", value: 10 },
                    ]
                },
                {
                    group: "用户管理",
                    mask: 0,
                    submodules: [
                        { name: "列出当前组下其他用户", value: 11 },
                        { name: "列出所有用户", value: 12 },
                        { name: "修改用户信息", value: 13 },
                        { name: "删除其他用户", value: 14 },
                        { name: "增加用户", value: 15 },
                    ]
                },
                {
                    group: "订单",
                    mask: 0,
                    submodules: [
                        { name: "修改客户订单价格", value: 21 },
                    ]
                },
                {
                    group: "日志管理",
                    mask: 0,
                    submodules: [
                        { name: "查看日志", value: 16 },
                        //{ name: "删除日志", value: 17 },
                    ]
                },
                {
                    group: "系统配置",
                    mask: 0,
                    submodules: [
                        { name: "权限管理", value: 18 },
                        //{ name: "普通系统配置", value: 19 },
                        { name: "修改系统配置", value: 20 },
                    ]
                },
            ]
        }),
        watch: {
            roleEdit: {
                deep: true,
                handler(val) {
                    console.log('value changed');
                    this.$emit("input", val);
                }
            },
            value() {

            }
        },

        created() {
            console.log(this.edit);
            if (this.edit)
                this.roleEdit = Object.assign({}, initRole, this.value);
            else
                this.roleEdit = Object.assign({}, initRole, {});
        }
    }
</script>

<style scoped>

</style>