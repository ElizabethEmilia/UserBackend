<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>开票服务统计</h3></Divider>
        <Alert type="success">
            温馨提示： 当前客户下公司的开票情况统计。
        </Alert>

        <PagedTable :columns="columns" :data-source="dataSource" />
    </Card>
</template>

<script>
import PagedTable from '../../pagedTable.vue';
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';

export default {
    components: { PagedTable },
    props: [ 'uid', 'cid' ],
    data: () => ({
        columns: [
            { title: '序号', type: 'index'},
            { title: '公司ID', key: 'cid'},
            { title: '申请公司', key: 'cName'},
            { title: '连续12个月收入(含税)', key: 'income12'},
            { title: '当月开普票次数', key: 'timeNormal'},
            { title: '当月开普票张数', key: 'countNormal'},
            { title: '当月开普票金额(含税)', key: 'amountNormal'},
            { title: '当月开票预缴税金', key: 'curPretax'} 
        ],
    }),
    computed: {
        dataSource() {
            return `/customer/${this.uid}/receipt/stat`;
        }
    },
    methods: {
    }

}
</script>
