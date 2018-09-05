<template>
    <div>
        <div class="card-margin">
            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />选择公司 </span>
                <Select v-model="cid" style="width:200px">
                    <Option v-for="(e,i) in companies" :value="e.id" :key="i">{{ e.lpname }}</Option>
                </Select>

            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />选择公司 </span>
                <Select v-model="ty" style="width:200px">
                    <Option v-for="(e,i) in receiptType" :value="i" :key="i">{{ e }}</Option>
                </Select>

            </div>
        </div>

    </div>
</template>

<script>
import '../../../../css/style.less';
import API from '../../../../js/api.js';
import { receiptType } from '../../../../constant';

export default {
    props: [ 'companies' ],
    data: () => ({
        cid: -1,
        ty: 0,
        receiptType,
    }),
    methods: {

    },
    mounted() {

    },
    watch: {
        cid(val) {
            this.$emit('on-select', { cid: this.cid, ty: this.ty });
        },
        ty(val) {
            this.$emit('on-select', { cid: this.cid, ty: this.ty  });
        }
    }
}
</script>

<style scoped>

</style>