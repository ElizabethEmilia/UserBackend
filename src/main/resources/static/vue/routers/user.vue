<template>
    <Card>

        <div style="margin: 10px">
            <Button type="success" @click="newRequest=true">新增管理员</Button>

            根据组筛选：
            <Select placeholder="选择要查看的组" v-model="selectedGroupID" style="display: inline-block; width: 300px;">
                <Option v-for="(e, i) in groups" :key="i" :value="e.id">{{e.name}}</Option>
            </Select>
            <a href="javascript:void(0)" v-if="selectedGroupID !== -1" @click="selectedGroupID = -1">取消筛选</a>
        </div>

        <PagedTable ref="data" :columns="columns" :data-source="ds" />

        <Modal v-model="dialogShouldShow" :title="selectedIndex == -1 ? '添加管理员' : '修改管理员信息'">
            <AdminInfo
                    :init-info="selectedAdmin"
                    v-if="dialogShouldShow"
                    @on-complete="val => { selectedIndex = -1; newRequest = false;  $refs.data.refresh(); }"
            >
            </AdminInfo>

            <div slot="footer" style="display: none">
            </div>
        </Modal>
    </Card>
</template>

<script>
import PagedTable from '../pagedTable.vue';
import AdminInfo from './user/admininfo.vue';
import { adminTypes } from '../../constant.js';
import API from '../../js/api.js';
import util from '../../js/util.js';
import render from '../../js/render.js';

export default {
    components: {
        PagedTable, AdminInfo,
    },
    data: function() {
        let _this = this;

        return {
            selectedIndex: -1,
            newRequest: false,

            selectedGroupID: -1,

            groups: [],

            columns() {
                let self = this;

                return [
                    { title: '序号', type: 'index', width: 100 },
                    { title: '名称', key: 'name', },
                    {
                        title: '权限值',
                        render: (h, p) => h('span', {}, adminTypes[this.tableData[p.index].roleValue]),
                    },
                    { title: '电话号码', key: 'phone' },
                    {
                        title: '操作',
                        width: 150,
                        render: (h,p) => h('div', [
                            render.link(h, p, '删除', async function() {
                                let obj = self.d[p.index];
                                await util.MessageBox.ComfirmAsync(self, "确认要删除管理员`" + obj.name + "`吗？");
                                try {
                                    await API.Admin.remove(obj.id);
                                    util.MessageBox.Show(self, "删除成功");
                                    self.refresh();
                                }
                                catch (e) {
                                    console.error(e);
                                    util.MessageBox.Show(self, "删除失败  " + (typeof e.msg !== "undefined"? e.msg: ""));
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
        selectedAdmin() {
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
        },
        ds() {
            if (this.selectedGroupID == -1)
                return "admin/list";
            else
                return "group/" + this.selectedGroupID + "/user";
        }
    },
    methods: {
        async getGroups() {
            this.groups = await API.Group.getSimplifiedList();
        }
    },
    mounted() {
        this.getGroups();
    }
}
</script>