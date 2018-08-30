<template>
    <a>Paged Table</a>
</template>

<script>

// 带有分页和数据显示，数据操作的Table
//TODO:  所有的请求均以特定的形式得到请求内容
/*
props:
    - columns: 和iview column name同（为了在render中可访问数据,需要传入data）、
    - data: 数据
    - ajaxURL: 数据来源的ajax请求
      - url?count              获取数量
      - url?page=1&size=10     获取第1页的内容
      - url?page=1             获取第1页的内容
    
    * pageChange回调和onPageSizeChange回调由组件实现

*/

export default (function(){
    var Data;
    
    return ({
        props: [ 'tableData' ],
        data: () => ({
            tableColumns: [
                { title: 'test', render: (h, p)=>h('span', {}, Data[i]); }
            ],
        }),
        created() {
            Data = this.tableData;
        },
    });
})();

</script>