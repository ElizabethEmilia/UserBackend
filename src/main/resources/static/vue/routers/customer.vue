<template>
    <div>
        <CustomerList 
            @on-select="u=>selectedUser=u" 
            @on-select-company="c=>selectedCompany=c"
            @on-back-to-list="u=>selectedUser={uid:-1}"
            v-show="selectedCompany.id < 0"
        />
        <!-- 暂时不放在这里 -->
        <!--CustomerCompantInformation 
            :admin="true" 
            v-if="selectedUser.uid > 0 &&  selectedCompany.id > 0"
            :cid="selectedCompany.uid"
        /-->
        <div v-if="selectedCompany.id > 0">
            <CustomerCompanyOverview
                    :comData="selectedCompany"
                    :uid="selectedUser.uid"
                    :cid="selectedCompany.id"
                    @on-back-to-user-list="selectedCompany={id: -1}"
            />
            <CustomerCompanySetup
                    :cid="selectedCompany.id"
            />
            <CustomerCompanyCertificate
                    :cid="selectedCompany.id"
            />
        </div>
        <div v-else-if="selectedUser.uid > 0">
            <CustomerPublic :uid="selectedUser.uid" />
            <CustomerOrder :uid="selectedUser.uid" />
            <CustomerReceipt :uid="selectedUser.uid" />
            <CustomerReceiptStat :uid="selectedUser.uid" />
        </div>

    </div>
</template>

<script>
import CustomerList from './customer/customerlist.vue';
import CustomerCompantInformation from './company/information.vue';
import CustomerPublic from './customer/cuspublic.vue';
import CustomerOrder from './customer/cusorder.vue';
import CustomerReceipt from './customer/cusreceipt.vue';
import CustomerReceiptStat from './customer/cusreceiptstat.vue';
import CustomerCompanyOverview from './customer/companyoverview.vue';
import CustomerCompanySetup from './customer/comsetup.vue';
import CustomerCompanyCertificate from './customer/comcert.vue';

export default {
    components: {
        CustomerList, CustomerCompantInformation, CustomerPublic, CustomerOrder,
        CustomerReceipt, CustomerReceiptStat, CustomerCompanyOverview,
        CustomerCompanySetup, CustomerCompanyCertificate,
    },
    data: () => ({
        selectedCompany: { id: -1 },
        selectedUser: { uid: -1 },
    }),
    computed: {
        selectedCompanyID() { return this.selectedCompany.id; },
        selectedUserID() { return this.selectedUser.uid; }
    }
}
</script>