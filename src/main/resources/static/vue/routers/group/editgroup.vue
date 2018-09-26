<template>
    <div>
        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />组名称 </span>
            <Input v-model="groupInfo.name" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> 组描述 </span>
            <Input v-model="groupInfo.remark" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin: 10px; text-align: right">
            <Button type="success" @click="create" v-if="!isEdit">创建组</Button>
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

            groupInfo: Object.assign({ password: '' }, init.tGroup),
        }),
        methods: {
            async create() {
                if (!this.checkRequiredFields()) {
                    return util.MessageBox.Show(this, "请填写所有必填项");
                }
                try {
                    await API.Group.add(this.groupInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.groupInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败, " + e.message);
                }
            },
            async modify() {
                if (!this.checkRequiredFields()) {
                    return util.MessageBox.Show(this, "请填写所有必填项");
                }
                try {
                    await API.Group.modify(this.initInfo.id, this.groupInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.groupInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败, " + e.message);
                }
            },
            checkRequiredFields() {
                return !(util.String.isNullOrEmpty(this.groupInfo.name));
            },
        },
        computed: {
            isEdit() {
                return typeof this.initInfo !== "undefined";
            }
        },
        mounted() {
            if (typeof this.initInfo !== "undefined") {
                this.groupInfo = this.initInfo;
                this.groupInfo = {
                    name: this.initInfo.name,
                    remark: this.initInfo.remark,
                };
            }
        },
        watch: {

        }

    }
</script>

<style scoped>

</style>