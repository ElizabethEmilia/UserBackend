<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>公司证照</h3></Divider>
        
        <PagedTable :columns="columns" :data-source="`company/${cid}/cert`" />
        <div style="margin-top: 20px;">

        </div>
    </Card>
</template>

<script>
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import PagedTable from '../../pagedTable.vue';
    
export default {
    props: [ 'cid' ],
    components: {
        PagedTable
    },
    data: () => ({
        columns() {
            let self = this;
            return [
                { title: '序号', type: 'index' },
                { title: '公司ID', key: 'name' },
                { title: '公司名称', key: 'cid' },
                { title: '证件编号', key: 'certNo' },
                { title: '证件名称', key: 'certName' },
                { 
                    title: '操作', 
                    key: 'action', 
                    render: (h, params) => {
                        console.log(h, params);
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click() {
                                        //console.log("sdasdsads",params);
                                        window.open('/res/avatar/' + params.row.certImg)
                                    }
                                }
                            }, '查看'),
                        ]);
                    }
                },
            ];
        },
    }),
    methods: {
    },
}
</script>

<style>

</style>
