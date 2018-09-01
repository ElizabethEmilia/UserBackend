<template>
    <div>
        <Table border :current="page" :columns="columns" :data="tableData"></Table>        
        <Page :total="count" show-sizer show-elevator @on-change="handlePageChange" @on-page-size-change="handleSizeChange" />
    </div>
</template>

<script>

// 带有分页和数据显示，数据操作的Table
//TODO:  所有的请求均以特定的形式得到请求内容
/*
props:
    - columns: 和iview column name同（为了在render中可访问数据,需要传入data）、
    - data-source: 数据来源的ajax请求
      - url?count              获取数量
      - url?page=1&size=10     获取第1页的内容
      - url?page=1             获取第1页的内容
    - external: 外部控制的值和回调
    
    * pageChange回调和onPageSizeChange回调由组件实现
     
    事件:  pagechanged  页面改变时产生
           sizechanged  大小改变时产生
           onajaxerror  获取ajax内容发生错误时候调用     

    例如： <PagedTable data-source="admin" :columns="dataColumns" />
*/

import $ from '../js/ajax.js';

export default (function(){
    return ({
        props: [ 'columns', 'dataSource' ],
        data() {
            return ({
                tableData: [],
                count: 0,
                size: 10,
                page: 1,
            }); 
        },
        created() {
            //Data = this.tableData;
        },
        methods: {
            handlePageChange(p) {
                this.page = p;
                this.getContentOfPage(p, this.size);
                this.$emit('pagechanged', p);
            },
            handleSizeChange(s) {
                this.size = s;
                this.getContentOfPage(this.page, s);
                this.$emit('sizechanged', s);
            },
            async getContentOfPage(page, size=10) {
                console.log(`[PagedTable] get content of page=${page}, size=${size}`);
                let url = this.requestUrl + `?page=${page}&size=${size}`;
                try {
                    let result = await $.ajax(url);
                    if (result.code != 0) {
                        throw new Error(result);
                    }
                    this.tableData = result.data;
                    console.log(`[PagedTable] success: page=${page}, size=${size}`);
                }
                catch(err) {
                    console.error(`[PagedTable] failed: page=${page}, size=${size}`, err);
                    this.$emit('onajaxerror', err);
                }
            },
            async getCount() {
                console.log(`[PagedTable] get count`);
                let url = this.requestUrl + `?count`;
                try {
                    this.count = await $.ajax(url);
                    console.log(`[PagedTable] count: ${this.count}`);
                }
                catch(err) {
                    console.error(`[PagedTable] get count failed`, err);
                    this.$emit('onajaxerror', err);
                }
            },
        },
        computed: {
            requestUrl() {
                return '/api/' + this.dataSource;
            }
        },
        watch: {
            dataSource(val) {
                console.log('[PagedTable] Source changed', val);
                this.page = 1;
                this.getContentOfPage(1, this.size);
            }
        },
        mounted() {
            this.getContentOfPage(1, 10);
        },
    });
})();

</script>