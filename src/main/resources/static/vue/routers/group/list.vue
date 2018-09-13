<template>
    <Card>

        <div style="margin: 10px">
            <Button type="success" @click="newRequest=true">新增组</Button>
        </div>

        <PagedTable ref="data" :columns="columns" data-source="group/list" />

        <Modal v-model="dialogShouldShow" :title="selectedIndex == -1 ? '添加组' : '编辑组信息'">
            <EditGroup
                    :init-info="selectedGroup"
                    v-if="dialogShouldShow"
                    @on-complete="val => { selectedIndex = -1; newRequest = false; $refs.data.refresh(); }"
            >
            </EditGroup>

            <div slot="footer" style="display: none">
            </div>
        </Modal>
    </Card>
</template>

<script>
    import PagedTable from '../../pagedTable.vue';
    import EditGroup from './editgroup.vue';
    import render from '../../../js/render.js';
    import util from '../../../js/util.js';
    import API from '../../../js/api.js';

    export default {
        components: {
            PagedTable, EditGroup,
        },
        data: function() {
            let _this = this;

            return {
                selectedIndex: -1,
                newRequest: false,

                columns() {
                    let self = this;

                    return [
                        { title: '序号', type: 'index', width: 100 },
                        { title: '名称', key: 'name', },
                        { title: '说明', key: 'remark' },
                        {
                            title: '操作',
                            width: 150,
                            render: (h,p) => h('div', [
                                render.link(h, p, '删除', async function() {
                                    let obj = self.d[this.selectedIndex];
                                    await util.MessageBox.ComfirmAsync(self, "确认要删除`" + obj.name + "`分组吗？");
                                    try {
                                        await API.Group.remove(obj.id);
                                        util.MessageBox.Show(self, "删除成功");
                                        self.refresh();
                                    }
                                    catch (e) {
                                        console.error(e);
                                        util.MessageBox.Show(self, "删除失败  " + typeof e.msg !== "undefined"? e.msg: "");
                                    }
                                }),
                                render.link(h, p, '查看详情', function() {
                                    _this.selectedIndex = p.index;
                                })
                            ])
                        },
                    ]
                }
            }
        },
        computed: {
            selectedGroup() {
                return this.$refs.data.d[this.selectedIndex];
            },
            dialogShouldShow: {
                get() {
                    return this.newRequest || this.selectedIndex >= 0;
                },
                set(val) {
                    this.newRequest = false;
                    this.selectedIndex = -1;
                }
            }
        }
    }
</script>