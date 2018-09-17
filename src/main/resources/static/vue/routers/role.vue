<template>
    <Card dis-hover>
        <Divider>
            <h2>角色权限管理</h2>
        </Divider>
        <div>
            <div class="cdmargin">
                <Button type="success" @click="v => {(isEdit = false); (selected={}); (editModalVisible = true)}">新增角色</Button>
            </div>
            <div class="cdmargin">
                <PagedTable ref="datatable" data-source="role/list" :columns="columns" @onrecvdata="val =>data=val" />
            </div>

            <Modal
                    :title="!isEdit ? '新增角色' : '编辑角色'"
                    v-model="editModalVisible"
                    @on-ok="() => isEdit ? updateRole():newRole()"
                    :width="800"
            >
                <RoleEdit v-if="editModalVisible" :edit="isEdit" v-model="selected" />
            </Modal>

        </div>
    </Card>
</template>

<script>
    import PagedTable from '../pagedTable.vue';
    import RoleEdit from './role/edit.vue';
    import render from '../../js/render.js';
    import API from '../../js/api.js';
    import util from '../../js/util.js';
    import init from '../../js/init.js';

    export default {
        components: {
            PagedTable, RoleEdit,
        },
        data() {
            let _this = this;
            return {
                columns() {
                    let self = this;
                    return [
                        { title: "序号", type: 'index' },
                        { title: '角色名', key: 'name' },
                        { title: '权限值', key: 'value' },
                        {
                            title: '操作',
                            render: (h,p)=> {
                                return h('div', [
                                    render.link(h,p,'查看/编辑', (p)=>{
                                        console.log(self.d[p.index].name);
                                        console.log(self.d[p.index].value);
                                        _this.isEdit = true;
                                        _this.selected = self.d[p.index];
                                        _this.editModalVisible = true;
                                    }),
                                    render.link(h,p,'删除', async (p)=>{
                                        try {
                                            await util.MessageBox.ComfirmAsync(self, "确定要删除该角色吗？");
                                            await API.Role.remove(self.d[p.index].id);
                                            util.MessageBox.Show(this, "删除角色成功");
                                            this.$refs.datatable.refresh();
                                        }
                                        catch(err) {
                                            console.error(err);
                                            util.MessageBox.Show(this, "删除角色失败" + err.msg ? ":" + msg: "");
                                        }
                                    })
                                ]);
                            }
                        }
                    ];
                },
                modalRoleObj: null,
                editModalVisible: false,
                data: [],
                isEdit: false,
                selected: init.tRole,
            }
        },
        methods: {
            async updateRole() {
                if (util.String.isNullOrEmpty(this.selected.name)) {
                    return util.MessageBox.Show(this, "请输入角色名");
                }
                try {
                    await API.Role.modify(this.selected.id, this.selected);
                    this.$refs.datatable.refresh();
                }
                catch(err) {
                    console.log(err);
                    return util.MessageBox.Show(this, "修改角色失败");
                }
            },
            async newRole() {
                if (util.String.isNullOrEmpty(this.selected.name)) {
                    return util.MessageBox.Show(this, "请输入角色名");
                }
                try {
                    await API.Role.addNew(this.selected);
                    this.$refs.datatable.refresh();
                }
                catch(err) {
                    console.log(err);
                    return util.MessageBox.Show(this, "修改角色失败");
                }
            }
        },
        watch: {

        },
        mounted: {

        }
    }
</script>

<style scoped>
.cdmargin { margin: 10px 0 10px; }
</style>