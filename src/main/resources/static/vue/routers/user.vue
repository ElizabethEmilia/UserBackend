<template>
    <Card>

        <div style="margin: 10px">
            <Button type="success" @click="newRequest=true">新增管理员</Button>
        </div>

        <PagedTable ref="data" :columns="columns" data-source="admin/list" />

        <Modal v-model="dialogShouldShow" title="添加管理员">
            <AdminInfo
                    :init-info="selectedAdmin"
                    v-if="dialogShouldShow"
                    @on-complete="val => { selectedIndex = -1; newRequest = false; }"
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
import $ from '../../js/ajax.js';
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
        }
    }
}
</script>