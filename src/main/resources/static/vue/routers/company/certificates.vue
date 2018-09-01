<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>公司证照</h3></Divider>
        
        <PagedTable v-if="selected != -1" :columns="columns" :data-source="`company/${cid}/cert`" />
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
        columns: [
            { title: '序号', type: 'index' },
            { title: '公司ID', key: 'name' },
            { title: '公司名称', key: 'cid' },
            { title: '证件编号', key: 'certNo' },
            { title: '证件名称', key: 'certNo' },
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
                                    console.log('click', this);
                                    console.log('index: 打印' + params.index);
                                }
                            }
                        }, '打印'),
                    ]);
                }
            },
        ]
    }),
    methods: {
    },
}
</script>

<style>

</style>
