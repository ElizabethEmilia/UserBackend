<template>
    <div>
        <Button type="success"
                v-if="selectedUser.uid < 0"
                @click="addCustomerDialogShouldShow = true;"
        >增加客户</Button>

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

        <Modal v-model="addCustomerDialogShouldShow">
            <div slot="footer">
                <Button type="success"
                        @click="addNewCustomer"
                >确定</Button>
                <Button
                        @click="addCustomerDialogShouldShow = false;"
                >取消</Button>
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />客户名称 </span>
                <Input v-model="infoSave.name" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />会员类别 </span>
                <Select v-model="infoSave.type" style="width:200px">
                    <Option v-for="(e,i) in memberType" :value="i" :key="i">{{ e }}</Option>
                </Select>
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />所属行业 </span>
                <Select v-model="infoSave.industry" style="width:200px">
                    <Option v-for="(e,i) in industry" :value="i" :key="i">{{ e }}</Option>
                </Select>
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input"> <i class="required" />手机号 </span>
                <Input v-model="infoSave.phone" placeholder="" style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">邮箱 </span>
                <Input v-model="infoSave.email" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">微信号 </span>
                <Input v-model="infoSave.wechat" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">QQ </span>
                <Input v-model="infoSave.qq" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">传真 </span>
                <Input v-model="infoSave.fax" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">其他手机号 </span>
                <Input v-model="infoSave.others" placeholder="" clearable style="width: 200px" />
            </div>

            <div style="margin-bottom: 5px;">
                <span class="title-before-input">地址 </span>
                <SelectArea v-model="area"></SelectArea>
                <p style="margin-top: 5px;">
                    <span class="title-before-input"> </span>
                    <Input v-model="infoSave.address" placeholder="" clearable style="width: 300px" />
                </p>

            </div>

        </Modal>

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
import init from '../../js/init.js';
import API from '../../js/api.js';
import util from '../../js/util.js';
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../constant.js';


export default {
    components: {
        CustomerList, CustomerCompantInformation, CustomerPublic, CustomerOrder,
        CustomerReceipt, CustomerReceiptStat, CustomerCompanyOverview,
        CustomerCompanySetup, CustomerCompanyCertificate,
    },
    data: () => ({
        selectedCompany: { id: -1 },
        selectedUser: { uid: -1 },

        addCustomerDialogShouldShow: false,
        infoSave: init.tCustomer,

        industry,
        memberType,
        pending: false
    }),
    computed: {
        selectedCompanyID() { return this.selectedCompany.id; },
        selectedUserID() { return this.selectedUser.uid; }
    },
    methods: {
        async addNewCustomer() {
            if (this.pending)
                return;

            let I = this.infoSave;
            if (util.String.isNullOrEmpty(I.name) ||
                util.String.isNullOrEmpty(I.phone) ||
                I.type === -1 || I.industry === -1)
                return util.MessageBox.Show(this, "请填写所有必填信息");

            if (window.config.adminGroupID !== I.gid) {
                // 为别的组添加
                if (window.config.P.AdminCustomerListOfCurrentGroup || window.config.P.AdminCustomerListAll);
                else return util.MessageBox.Show(this, "该管理员账户没有权限为其他组用户添加用户");
            }

            try {
                await API.Customer.addNew(this.infoSave);
                util.MessageBox.Show(this, "操作成功");
                this.addCustomerDialogShouldShow = false;
            }
            catch(e) {
                console.error(e);
                util.MessageBox.Show(this, '操作失败');
            }
        }
    }
}
</script>