<template>
    <Poptip trigger="focus">
        <InputNumber :step="100" placeholder="" v-model="amount" style="width: 200px; font-size: 14px" />                    
        <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber }}</span></div>
    </Poptip>
</template>

<script>
export default {
    props: [ 'value' ],
    data: () => ({
        amount: 0,
    }),
    watch: {
        amount(val) {
            this.$emit('input', val);
        }
    },
    computed: {
        formatNumber () {
            if (this.amount === '') return '0';
            function parseNumber(str) {
                return str.split(/(?=(\d{3})+$)/g).filter((e,i)=>i%2==0).join(',');
            }
            let [str, prec] = (''+this.amount).split('.');
            return parseNumber(str) + (prec ? '.'+prec : '');
        }
    }
}
</script>
