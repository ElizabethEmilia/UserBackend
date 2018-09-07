<template>
    <div>
        <SelectCompany @on-select-company="hSelectCompany" />
        <div v-if="cid != -1">
            <CompanyOverview :cid="cid" :comData="comdata" />
            <SetupProgress :cid="cid" />
            <Certificates :cid="cid" />
        </div>
    </div>
</template>

<script>
import SetupProgress from './company/setupprogress.vue';
import SelectCompany from './company/selectcompany.vue';
import Certificates from './company/certificates.vue';
import Information from './company/information.vue';
import CompanyOverview from './company/overview.vue';

export default {
    components: {
        SetupProgress, SelectCompany, Certificates, CompanyOverview,
    },
    data: () => ({
        cid: -1,
        comdata: null,
    }),
    methods: {
        hSelectCompany(d) {
            this.comdata = d;
            this.cid = d.id;
            console.log('[Select Company]', d);
        }
    }
}
</script>