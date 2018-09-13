<template>
    <Card>

        <div style="margin: 10px">
            <Button type="success" @click="newRequest=true">新增组</Button>
        </div>

        <PagedTable ref="data" :columns="columns" data-source="group/list" />

        <Modal v-model="dialogShouldShow" title="添加组">
            <EditGroup
                    :init-info="selectedGroup"
                    v-if="dialogShouldShow"
                    @on-complete="val => { selectedIndex = -1; newRequest = false; }"
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