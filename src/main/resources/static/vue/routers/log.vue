<template>
    <Card>
        <div style="margin: 10px;">
            <Input placeholder="搜索用户名或记录描述" v-model="search1" />
        </div>

        <PagedTable :columns="columns" data-source="system/log" :additional-params="sp" />
    </Card>
</template>

<script>
    import PagedTable from '../pagedTable.vue';
    import { adminTypes } from '../../constant.js';
    import util from '../../js/util.js';
    import debounce from "lodash.debounce"

    export default {
        components: {
            PagedTable
        },
        data: () => ({
            search: '',
            search1: '',
            columns: function() {
                //console.log('func', this);
                let $this = this;
                return [
                    //{ type: 'selection', width: 60, align: 'center' },
                    { title: '序号', type: 'index', width: 100 },
                    { title: '管理员名称', key: 'name', width: 250 },
                    {
                        title: '时间',
                        render: (h,p)=>h('span',{},util.Date.toTimeString(util.Date.toDateSafe(p.row.tm))),
                        width: 200,
                    },
                    { title: '描述', key: 'description', width: 600 },

                ]
            },
        }),
        computed: {
            sp() {
                return util.forGetParams({
                    search: this.search,
                });
            },
        },
        watch: {
            search1: debounce(function () {
                this.search = this.search1;
            }, 1000)
        }
    }
</script>