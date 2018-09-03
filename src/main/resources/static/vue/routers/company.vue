<template>
    <div>
        <SelectCompany @on-select-company="hSelectCompany" />
        <div v-if="cid != -1">
            <SetupProgress :cid="cid" />
            <Certificates :cid="cid" />
            <Information :cid="cid" :data="comdata" />
        </div>
    </div>
</template>

<script>
import SetupProgress from './company/setupprogress.vue';
import SelectCompany from './company/selectcompany.vue';
import Certificates from './company/certificates.vue';
import Information from './company/information.vue';

export default {
    components: {
        SetupProgress, SelectCompany, Certificates, Information,
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