<template>
    <div>
        <Overview />
        <ModifyPassword />
        <OnlineCharge />
        <PublicCharge />
        <ChargeDetail />
        <OrderList />
    </div>
</template>

<script>
import PagedTable from '../pagedTable.vue';
import ModifyPassword from './account/modifyPassword.vue';
import ChargeDetail from './account/chargeDetail.vue';
import OrderList from './account/orderlist.vue';
import PublicCharge from './account/public.vue';
import OnlineCharge from './account/onlineCharge.vue';
import Overview from './account/overview.vue';

export default {
    components: {
        PagedTable, ModifyPassword, ChargeDetail, OrderList, PublicCharge,
        OnlineCharge, Overview
    },
    data: () => ({ }),
    methods: {  }
}
</script>

<style>

</style>