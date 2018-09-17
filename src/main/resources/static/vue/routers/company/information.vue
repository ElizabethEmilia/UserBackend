<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>{{ pending ? '公司信息':info.name }}</h3></Divider>
        
        <div style="margin-top: 20px;">
            <Table :show-header="false" :columns="col" :data="data"/>
        </div>

    </Card>
</template>

<script>

/**
 * 参数：
 *    admin   是否是管理员端
 * 
 */

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
    
export default {
    props: [ 'cid', 'admin' ],
    data: () => ({
        col: [
            { key: 'k' },
            { key: 'v' }
        ],
        pending: true,
        info: {
            id: 1,
            name: '加载中...',
            taxType: 1,
            vatType: 1,
            yasType: 1,
            preTaxRetio: 1,
            vaTaxRatio: 1,
            vatrFreq: 1,
            cbTax: 1,
            eaTax: 1,
            leaTax: 1,
            riverTax: 1,
            entOrgTax: 1,
            invType: 1,
            businessType: 1,
            taxPackStart: 0,
            taxPackEnd: 0,
            oriTaxPackStart: 1,
            oriTaxPackEnd: 1,
            tmFirstEx: 1,
            status: 0,
            lpname: '',
        }
    }),
    methods: {
        async getInfo() {
            let result = await $.ajax(`/api/company/${this.cid}/info`);
            if (result.code === 0) {
                this.info = result.data;
                this.pending = false;
            }
        },
        async getInfoAdmin() {
            let result = await $.ajax(`/api/customer/_/company/${this.cid}`);
            if (result.code === 0) {
                this.info = result.data;
                this.pending = false;
            }
        }
    },
    computed: {
        data() {
            return Object.keys(this.info).map(e=>({k:e,v:this.info[e]}));
        }
    },
    mounted() {
        if (typeof this.admin != "undefined" && this.admin == true)  this.getInfoAdmin();
        else this.getInfo();
    }
}
</script>

<style>

</style>
