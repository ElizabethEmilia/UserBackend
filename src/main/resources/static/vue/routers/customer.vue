<template>
    <div>
        <Button type="success"
                v-if="selectedUser.uid < 0"
                @click="showDialog"
                style="margin-bottom: 12px;"
        >增加客户</Button>

        <CustomerList
                ref="list"
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
                <Checkbox v-model="addForOthers">
                    为其他管理员添加
                </Checkbox>
            </div>

            <div style="margin-bottom: 5px;" v-show="addForOthers">
                <span class="title-before-input"> <i class="required" />所属组 </span>
                <Select v-model="selectedGid" style="width:200px">
                    <Option v-for="(e,i) in groups" :value="e.id" :key="e.id">{{ e.name }}</Option>
                </Select>
            </div>
            <div style="margin-bottom: 5px;" v-show="addForOthers">
                <span class="title-before-input"> <i class="required" />所属管理员 </span>
                <Select v-model="selectedAid" style="width:200px">
                    <Option v-for="(e,i) in admins" :value="e.id" :key="e.id">{{ e.name }}</Option>
                </Select>
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
import SelectArea from '../components/areaselect.vue';
import init from '../../js/init.js';
import API from '../../js/api.js';
import util from '../../js/util.js';
import md5 from 'js-md5';
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../constant.js';


export default {
    components: {
        CustomerList, CustomerCompantInformation, CustomerPublic, CustomerOrder,
        CustomerReceipt, CustomerReceiptStat, CustomerCompanyOverview,
        CustomerCompanySetup, CustomerCompanyCertificate, SelectArea,
    },
    data: () => ({
        selectedCompany: { id: -1 },
        selectedUser: { uid: -1 },

        addForOthers: false,

        addCustomerDialogShouldShow: false,
        infoSave: init.tCustomer,

        industry,
        memberType,
        pending: false,

        selectedGid: -1,
        selectedAid: -1,

        groups: [],
        admins: [],
    }),
    computed: {
        selectedCompanyID() { return this.selectedCompany.id; },
        selectedUserID() { return this.selectedUser.uid; },
        // 城市地址
        area: {
            get() {
                return [ this.infoSave.province, this.infoSave.city, this.infoSave.district ];
            },
            set(val) {
                [ this.infoSave.province, this.infoSave.city, this.infoSave.district ] = val;
            }
        },
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

            // add for others
            if (!this.addForOthers) {
                I.aid = window.config.adminID;
            }
            else {
                if (this.selectedAid === -1) {
                    return util.MessageBox.Show(this, "请选择一个管理员");
                }
                I.aid = this.selectedAid;
            }

            // set default password
            I.password = md5(I.phone);

            try {
                await API.Customer.addNew(I);
                util.MessageBox.Show(this, "添加用户成功，默认用户密码为手机号码");
                this.$refs.list.$refs.dt.refresh();
                this.addCustomerDialogShouldShow = false;
            }
            catch(e) {
                console.error(e);
                util.MessageBox.Show(this, '操作失败,' + e.message);
            }
        },
        showDialog() {
            if (!window.config.P.AdminCustomerAdd) {
                util.MessageBox.Show(this, "该账号没有权限添加用户");
                return;
            }
            this.addCustomerDialogShouldShow = true;
        },
        async loadGroups() {
            this.groups = await API.Group.getSimplifiedList();
        },
        async loadAdmins() {
            this.admins = await API.Group.getUserOfGroup(this.selectedGid);
        }
    },
    watch: {
        addForOthers(val) {
            if (!val) return;
            if (window.config.P.AdminCustomerListOfCurrentGroup || window.config.P.AdminCustomerListAll);
            else{
                util.MessageBox.Show(this, "该账号没有权限为其他人添加用户");
                this.addForOthers = false;
                return;
            }
            this.loadGroups();
            this.selectedGid =window.config.adminGroupID;
            this.selectedAid =window.config.adminID;
        },
        selectedGid(val) {
            if (val === -1) {
                return this.admins = [];
            }
            else if (window.config.adminGroupID !== val) {
                // 为别的组添加
                if (window.config.P.AdminCustomerListAll);
                else
                    return util.MessageBox.Show(this, "该管理员没有权限为其他组用户添加用户");
            }
            this.loadAdmins();
            this.selectedAid = -1;
        }
    }
}
</script>