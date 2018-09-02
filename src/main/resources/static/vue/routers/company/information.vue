<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>公司信息</h3></Divider>
        
        <div style="margin-top: 20px;">
            <Table :show-header="false" :columns="col" :data="data"/>
        </div>

    </Card>
</template>

<script>
import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
    
export default {
    props: [ 'cid' ],
    data: () => ({
        col: [
            { key: 'k' },
            { key: 'v' }
        ],
        info: {
            id: 1,
            lpname: '大乔科技工作室',
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
        }
    }),
    methods: {
        async getInfo() {
            let result = await $.ajax(`/api/company/${cid}/info`);
            if (result.code === 0) {
                this.info = result.data;
            }
        }
    },
    computed: {
        data() {
            return Object.keys(this.info).map(e=>({k:e,v:this.info[e]}));
        }
    },
    mounted() {
        this.getInfo();
    }
}
</script>

<style>

</style>
