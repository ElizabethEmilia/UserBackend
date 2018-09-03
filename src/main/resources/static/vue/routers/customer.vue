<template>
    <div>
        <CustomerList 
            @on-select="u=>selectedUser=u" 
            @on-select-company="c=>selectedCompany=c" 
            @on-back-to-list="u=>selectedUser={uid:-1}"
        />
        <!-- 暂时不放在这里 -->
        <!--CustomerCompantInformation 
            :admin="true" 
            v-if="selectedUser.uid > 0 &&  selectedCompany.id > 0"
            :cid="selectedCompany.uid"
        /-->
        <CustomerPublic 
            v-if="selectedUser.uid > 0"
            :uid="selectedUser.uid"
        />
    </div>
</template>

<script>
import CustomerList from './customer/customerlist.vue';
import CustomerCompantInformation from './company/information.vue';
import CustomerPublic from './customer/cuspublic.vue';

export default {
    components: {
        CustomerList, CustomerCompantInformation, CustomerPublic,
    },
    data: () => ({
        selectedCompany: { id: -1 },
        selectedUser: { uid: -1 },
    }),
    computed: {
        selectedCompanyID() { return this.selectedCompany.id; },
        seletedUserID() { return this.selectedUser.uid; }
    }
}
</script>