<template>
    <div>
        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />组名称 </span>
            <Input v-model="groupInfo.name" placeholder="" clearable style="width: 200px" />
        </div>

        <div style="margin-bottom: 5px;">
            <span class="title-before-input"> <i class="required" />组描述 </span>
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
                try {
                    await API.Group.add(this.groupInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.groupInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败");
                }
            },
            async modify() {
                try {
                    await API.Group.add(this.groupInfo);
                    util.MessageBox.Show(this, "操作成功");
                    this.$emit("on-complete", this.groupInfo);
                }
                catch (e) {
                    console.log(e);
                    util.MessageBox.Show(this, "操作失败");
                }
            }
        },
        computed: {
            isEdit() {
                return typeof this.initInfo !== "undefined";
            }
        },
        mounted() {
            this.getGroup();
            this.getRole();
            if (typeof this.initInfo !== "undefined") {
                this.groupInfo = this.initInfo;
            }
        },
        watch: {

        }

    }
</script>

<style scoped>

</style>