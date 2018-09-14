<template>
    <div>
        <RangeSelect />
        <TaxAccount />
    </div>
</template>

<script>
import RangeSelect from './tax/range.vue';
import TaxAccount from './tax/taxaccount.vue';

export default {
    components: {
        RangeSelect, TaxAccount,
    },
    data: () => ({

    })
}
</script>