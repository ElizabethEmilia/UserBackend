<template>
    <div>
        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />管理员名称 </span>
            <Input v-model="adminInfo.name" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />角色 </span>
            <Select style="width: 200px" v-model="selectedRoleIndex">
                <Option
                        v-for="(e,i) in roles"
                        :key="i"
                        :value="i"
                >{{ e.name }}</Option>
            </Select>
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />所属组 </span>
            <Select style="width: 200px" v-model="selectedGroupIndex">
                <Option
                        v-for="(e,i) in groups"
                        :key="i"
                        :value="i"
                >{{ e.name }}</Option>
            </Select>
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />手机号码 </span>
            <Input v-model="adminInfo.phone" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />设置登录密码 </span>
            <Input v-model="adminInfo.password" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin: 10px; text-align: right">
            <Button type="success" @click="create" v-if="!isEdit">创建管理员</Button>
            <Button type="success" @click="modify" v-else>保存更改</Button>
            <Button @click="val => $emit('on-complete', null)">取消</Button>
        </div>



    </div>
</template>

<script>
    // 事件
    //   on-complete: 完成时触发

    import API from '../../../js/api.js';
    import util from '../../../js/util.js';
    import init from '../../../js/init.js';

    export default {
        props: [ 'initInfo' ],
        data: () => ({
            groups: [],
            roles: [],

            selectedRoleIndex: -1,
            selectedGroupIndex: -1,

            adminInfo: Object.assign({ password: '' }, init.tAdmin),
        }),
        methods: {
            async getGroup() {
                try {
                    this.groups = await API.Group.getSimplifiedList();
                    if (typeof this.initInfo !== "undefined") {
                        this.selectedGroupIndex = this.groups.map(e=>e.id).indexOf(this.initInfo.gid);
                    }
                }
                catch(err) {
                    console.error(err);
                }
            },
            async getRole() {
                try {
                    this.roles = await API.Role.getList();
                    if (typeof this.initInfo !== "undefined") {
                        this.selectedRoleIndex = this.roles.map(e=>e.id).indexOf(this.initInfo.roleid);
                    }
                }
                catch(err) {
                    console.error(err);
                }
            },
            async create() {
                if (!this.checkInput())
                    return util.MessageBox.Show(this, "请填写所有必填项");
                try {
                    await API.Admin.add(this.adminInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.adminInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败");
                }
            },
            async modify() {
                if (!this.checkInput())
                    return util.MessageBox.Show(this, "请填写所有必填项");
                try {
                    await API.Admin.modify(this.adminInfo.id, this.adminInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.adminInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败");
                }
            },
            checkInput() {
                return !(
                    util.String.isNullOrEmpty(this.adminInfo.name) ||
                    util.String.isNullOrEmpty(this.adminInfo.phone) ||
                    this.adminInfo.roleid === null ||
                    this.adminInfo.gid === null
                )
            }
        },
        computed: {
            selectedRole() {
                return this.roles[this.selectedRoleIndex];
            },
            selectedGroup() {
                return this.groups[this.selectedGroupIndex];
            },
            isEdit() {
                return typeof this.initInfo !== "undefined";
            }
        },
        mounted() {
            this.getGroup();
            this.getRole();
            if (typeof this.initInfo !== "undefined") {
                this.adminInfo = this.initInfo;
            }
        },
        watch: {
            selectedRoleIndex() {
                if (typeof this.selectedRole == "undefined")
                    return null;
                this.adminInfo.roleid = this.selectedRole.id;
            },
            selectedGroupIndex() {
                if (typeof this.selectedGroup == "undefined")
                    return null;
                this.adminInfo.gid = this.selectedGroup.id;
            }
        }

    }
</script>

<style scoped>

</style>