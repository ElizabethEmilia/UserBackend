<template>
    <!-- 账号总览 -->
    <div>

            <Alert type="warning" v-if="info.checked === 0">
                该客户信息待审核。
                <span v-if="P.CheckCompany">
                                    <a href="javascript:void(0)" @click="requestCheck('accept')">通过</a>
                                    <a href="javascript:void(0)" @click="requestCheck('reject')">拒绝</a>
                </span>
            </Alert>

            <Alert type="error" v-if="info.checked === 2">
                该客户信息已被拒绝。
                <span v-if="!P.CheckCompany">
                                    <a href="javascript:void(0)" @click="requestCheck('resubmit')">重新提交审核</a>
                </span>
            </Alert>

            <Row>
                <Col span="12">
                    <Row>
                        <div :style="avatar ? { backgroundImage: 'url(' + avatar + ')' }:{}" class="avatar float-left">
                            <div style="position: relative;" v-if="false">
                                <div :style="pendingUpload?{ opacity: 1 }:{}" class="avatar-mask" @click="chooseFile">
                                    {{ pendingUpload ? '上传中' : '更换头像' }}
                                    <input ref="openFile" @change="uploadFile" type="file" style="display:none" />
                                </div>
                            </div>
                        </div>
                        <div class="float-left" style="margin-left: 20px;">
                            <span style="font-size: 16px">{{ memberType[ info.type ] }} / </span>
                            <span style="font-size: 20px; color: #000">{{ info.name }}</span>
                            <p> {{ industry[info.industry] }} </p>
                        </div>
                    </Row>
                    <Row v-if="!editMode">
                        <Divider dashed orientation="right" ><a href="javascript:void(0)" @click="edit_basicInformation()">编辑</a></Divider>
                        <!-- 用户信息显示开始 -->
                        
                        <Row class="line-margin">
                            <Col span="12"><Icon type="ios-call" /> {{ info.phone }}</Col>
                            <Col span="12"><Icon type="ios-mail" /> {{ info.email }}  </Col>
                        </Row>
                        <Row class="line-margin">
                            <Col span="12"><Icon type="ios-chatboxes" /> {{ info.wechat }} </Col>
                            <Col span="12">QQ: {{ info.qq }}</Col>
                        </Row>
                        <Row class="line-margin">
                            <Col span="12"><Icon type="ios-print" /> {{ info.fax }}</Col>
                            <Col span="12"><Icon type="ios-navigate" /> {{ info.province }}/{{ info.city }}/{{ info.district }}</Col>
                        </Row>
                        <!-- 用户信息显示结束 -->
                    </Row>
                    <Row v-else>
                        <Divider dashed orientation="right" >
                            <a v-if="hashBeforeModify != hashAfterModify" @click="discardChanges()" href="javascript:void(0)" style="margin-right: 10px;">放弃更改</a>
                            <a href="javascript:void(0)" v-if="hashAfterModify == hashBeforeModify" @click="editMode=false">返回</a>
                            <a v-else :disabled="pendingSave || !P.AdminCustomerModify" href="javascript:void(0)" @click="saveChanges_basicInformation">{{ pendingSave?'正在':'' }}保存</a>
                        </Divider>
                        <Card :bordered="false" dis-hover>
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
                                <Input v-model="infoSave.phone" disabled placeholder="" style="width: 200px" />
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
                                <span class="title-before-input">地址 </span>
                                <SelectArea v-model="area"></SelectArea>
                                <p style="margin-top: 5px;">
                                    <span class="title-before-input"> </span>
                                    <Input v-model="infoSave.address" placeholder="" clearable style="width: 300px" />
                                </p>
                                
                            </div>

                        </Card>
                    </Row>
                    <Row>
                        <Card  :bordered="false" dis-hover  v-show="!editMode">
                            <p slot="title">客户余额</p>
                            <Row>
                                <Col span="12" style="text-align: center; padding-top: 13px;">
                                    <Card :bordered="false" dis-hover>

                                        余额：<span style="font-size: 25px; color: green">{{ loadingBanlance || String(Number((info.packBalance) + (info.taxBalance) + (info.otherBalance))) === "NaN" ? "--" : Number((info.packBalance) + (info.taxBalance) + (info.otherBalance)).toFixed(2) }}</span>
                                    </Card>
                                </Col>
                                <Col span="12">
                                    <!--Card :bordered="false" dis-hover>
                                        <Row>
                                            <Col span="12">收入金额总计</Col>
                                            <Col span="12" style="text-align: right">{{ Number(stats.income).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">最近一笔收入</Col>
                                            <Col span="12" style="text-align: right">{{ Number(stats.lastIncome).toFixed(2) }}</Col>
                                        </Row>
                                        <Divider dashed ></Divider>
                                        <Row>
                                            <Col span="12">支出金额总计</Col>
                                            <Col span="12" style="text-align: right">{{ Number(stats.outcome).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">最近一笔支出</Col>
                                            <Col span="12" style="text-align: right">{{ Number(stats.lastOutcome).toFixed(2)     }}</Col>
                                        </Row>
                                    </Card-->

                                    <Card :bordered="false" dis-hover>

                                        <Row>
                                            <Col span="12">年费余额</Col>
                                            <Col span="12" style="text-align: right">{{ loadingBanlance ? "--" : Number(info.packBalance).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">税金余额</Col>
                                            <Col span="12" style="text-align: right">{{ loadingBanlance ? "--" : Number(info.taxBalance).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">其他余额</Col>
                                            <Col span="12" style="text-align: right">{{ loadingBanlance ? "--" : Number(info.otherBalance).toFixed(2) }}</Col>
                                        </Row>
                                    </Card>
                                </Col>
                            </Row>

                        </Card>
                    </Row>

                </Col>
                <Col span="12">
                    <Card :bordered="false" dis-hover>
                        <p slot="title">客户的公司 ({{ companyCount }})</p>
                        <CellGroup v-if="companyCount > 0" @on-click="selectCompany">
                            <Cell v-for="(e,i) in companyList" :title="e.name" :key="i"
                                :name='i'
                            />
                        </CellGroup>
                        <p style="text-align: center; margin: 10px 0 10px 0" v-else>
                            {{ loading ? '正在加载列表...':'列表为空' }}
                        </p>
                    </Card>
                    
                    <Row>
                        <Card  :bordered="false" dis-hover>
                            <p slot="title">客户管理</p>
                            
                            <CellGroup @on-click="cellGroupClick">
                                <Cell v-if="P.ChargeForCustomer && info.checked === 1" name="charge" title="余额充值" />
                                <Cell v-if="P.ReceiptForCustomer && info.checked === 1" name="deduction" title="扣除余额" />
                                <Cell v-if="P.AdminCompanyAddAndModify && P.AdminCustomerListAll" name="aid" title="更改客户所属管理员" />
                                <!--Cell v-if="P.AdminCustomerModify" name="edit" title="编辑资料" /-->
                                <Cell v-if="P.AdminCompanyAddAndModify && info.checked === 1" name="newcompany" title="新增公司" />
                                <Cell v-if="P.AdminCustomerRemoval" name="remove" title="删除客户" style="color: red"/>
                            </CellGroup>

                        </Card>
                    </Row>
                </Col>
            </Row>

            <CustomerOverviewDialog v-model="sholdNewCompanyDialogOpen">

            </CustomerOverviewDialog>

            <Modal  v-model="aidDialogShouldShow" @on-ok="modifyaid">
                <div style="margin-bottom: 5px;">
                    <span class="title-before-input"> <i class="required" />所属组 </span>
                    <Select v-model="selectedGid" style="width:200px">
                        <Option v-for="(e,i) in groups" :value="e.id" :key="e.id">{{ e.name }}</Option>
                    </Select>
                </div>
                <div style="margin-bottom: 5px;">
                    <span class="title-before-input"> <i class="required" />所属管理员 </span>
                    <Select v-model="selectedAid" style="width:200px">
                        <Option v-for="(e,i) in admins" :value="e.id" :key="e.id">{{ e.name }}</Option>
                    </Select>
                </div>
            </Modal>

            <Modal v-model="chargeDialogShouldShow" title="余额充值">
                <div style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        充值类型
                    </span>
                    <Select v-model="chargeInfo.type" style="width: 100px;">
                        <Option label="年费" :value="0"></Option>
                        <Option label="税金" :value="1"></Option>
                        <Option label="其他" :value="2"></Option>
                    </Select>
                </div>

                <div style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        充值金额
                    </span>
                    <Poptip trigger="focus">
                        <InputNumber :step="100" placeholder="" v-model="chargeInfo.amount" style="width: 200px; font-size: 14px" />
                        <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber }}</span></div>
                    </Poptip>

                </div>

                <div style="text-align: right " slot="footer">
                    <Button @click="manualCharge" type="success" :loading="loading">充值余额</Button>
                    <Button @click="chargeDialogShouldShow = false">取消</Button>
                </div>

            </Modal>

            <Modal v-model="shouldDeductionDialodOpen" title="扣除余额">
                <div style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        扣费类型
                    </span>
                    <Select v-model="deduceInfo.type" style="width: 100px;">
                        <Option label="年费" :value="0"></Option>
                        <Option label="税金" :value="1"></Option>
                        <Option label="其他" :value="2"></Option>
                    </Select>
                </div>

                <div style="margin: 5px">
                    <span v-if="deduceInfo.type === 0">
                        扣除年费成功后，将自动为客户续期一年的服务包（若客户年费账户余额足以扣除年费）。
                    </span>
                    <span v-if="deduceInfo.type === 1">
                        扣除税金后，若税金账户为负数，将通知客户需要补交税金。
                    </span>

                </div>

                <div  v-show="deduceInfo.type !== 3"  style="margin-top: 5px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        公司
                    </span>
                    <Select v-model="deduceInfo.cid" style="width: 200px;">
                        <Option v-for="(e,i) in companyList" :label="e.name" :value="e.id" :key="i"></Option>

                    </Select>
                </div>

                <div style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        扣除金额
                    </span>
                    <Poptip trigger="focus">
                        <InputNumber :step="100" placeholder="" v-model="deduceInfo.amount" style="width: 200px; font-size: 14px" />
                        <div slot="content"><span style="font-size: 20px; color: green;">￥{{ formatNumber1 }}</span></div>
                    </Poptip>

                </div>

                <div style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">

                    </span>
                    <Checkbox v-model="deduceInfo.deduced">已扣</Checkbox>

                </div>


                <div  v-show="deduceInfo.type === 1"  style="margin-top: 20px; margin-left: 30px;">
                    <span style="font-size: 14px; width: 100px; display:inline-block">
                        凭证
                    </span>

                    <div style="position: relative">
                        <UploadFile v-model="deduceInfo.credit" title="上传支付凭证"/>
                    </div>
                </div>

                <div style="text-align: right " slot="footer">
                    <Button @click="manualDeduce" type="success" :loading="loading">扣除余额</Button>
                    <Button @click="shouldDeductionDialodOpen = false">取消</Button>
                </div>
            </Modal>
        </div>
</template>

<script>

import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';
import util from '../../../js/util.js';
import $ from '../../../js/ajax.js';
import md5 from 'js-md5';
import API from '../../../js/api.js';
import CustomerOverviewDialog from './dialog/newcompany.vue';
import SelectArea from '../../components/areaselect.vue';
import UploadFile from '../../components/uploadfile.vue';

/**
 * 事件
 * 
 * on-edit-mode-change: 
 * on-select-company:   选择公司的时候触发
 */

export default {
    components: {
        CustomerOverviewDialog, SelectArea, UploadFile,
    },
    props: [ 'cusData' ],
    data: () => ({
        info: {},
        infoSave: {},
        companyCount: 0,
        companyList: [],
        editMode: false,
        selectedCompany: -1,
        stats: {
            income: 0,
            lastIncome: 0,
            outcome: 0,
            lastOutcome: 0,
            balance: 0,
        },
        avatar: null,
        industry,
        memberType,
        provinces: ['四川省'],
        cities: ['攀枝花市'],
        district: ['东区', '西区'],
        pendingSave: false, //记录是否在保存
        pendingUpload: false, //是否正在上传头像
        loading: true,

        chargeDialogShouldShow: false,

        sholdNewCompanyDialogOpen: false,
        aidDialogShouldShow: false,
        shouldDeductionDialodOpen: false,

        deducted: false,

        P: window.config.P,

        chargeInfo: {
            type: 0,
            amount: 0,
        },
        deduceInfo: {
            type: 0,
            amount: 0,
            credit: '',
            cid: -1,
            deduced: false,
        },

        selectedGid: -1,
        selectedAid: -1,
        groups: [],
        admins: [],

        loading: false,

        loadingBanlance: false,
    }),
    methods: {
         // 基础信息的编辑
        edit_basicInformation() {
            this.editMode = true
            this.infoSave = Object.assign({}, this.info);
        },
        // 基础信息的保存
        async saveChanges_basicInformation() {
            if (this.pendingSave)
                return;
            let d = this.infoSave;
            let e = [];
            if (d.name == '') e.push('客户名称');
            if (d.type == -1) e.push('会员类型');
            if (d.industry == -1) e.push('所属行业');
            if (e.length != 0) {
                alert(e.join('、')+'不能为空');
                return;
            }

            this.pendingSave = true;
            try {
                let result = await $.ajax('/api/customer/' + this.infoSave.uid, this.infoSave);
                if (result.code === 0) {
                    // 保存成功
                    this.pendingSave = false;
                    this.editMode = false;
                    this.getBasicInfo();
                    return;
                }
                // 保存失败
                alert('保存失败：' + result.msg);
            }
            catch(err) {
                alert('保存失败, 通讯错误');
            }
            this.pendingSave = false;
        },
        // 通过file选择文件
        chooseFile() {
            if (this.pendingUpload) {
                return;
            }
            this.$refs.openFile.click();
        },
        // 上传文件修改头像
        async uploadFile(e) {
            if (this.pendingUpload)
                return;
            this.pendingUpload = true;
            try {
                let fileContent = await util.File.getFileContentAsync(this.$refs.openFile);
                this.avatar = fileContent.data;
                let result = await $.ajax('/api/account/avatar', { img: this.avatar });
                if (result.code != 0) {
                    alert(result.msg);
                    throw new Error(result.msg);
                }
                alert('上传头像成功');
            }
            catch(err) {
                console.error(err);
                alert('上传头像失败， ' + err.message);
            }
            this.pendingUpload = false;
        },
        // 获取用户基本信息
        async getBasicInfo() {
            this.loadingBanlance = true;
            try {
                let result = await $.ajax('/api/customer/' + this.info.uid);
                if (result.code) {
                    return alert('获取基本信息失败：' + result.msg);
                }
                this.info = result.data;
                this.avatar = result.data.avatar;
                this.loadingBanlance = false;
            }
            catch(err) {
                 util.Debug.ralert('获取基本信息失败');
            }
        },
        // 获取公司数量
        async getCompanyCount() {
            try {
                let result = await $.ajax('/api/company/count');
                if (result.code) {
                    return alert('获取公司数量失败：' + result.msg);
                }
                this.companyCount = result.data;
            }
            catch(err) {
                 util.Debug.ralert('获取公司数量失败');
            }
        },
        // 获取公司
        async getCompany() {
            try {
                let result = await $.ajax(`/api/customer/${ this.cusData.uid }/company/list?size=1000`);
                if (result.code) {
                    return alert('获取公司失败：' + result.msg);
                }
                this.loading = false;
                this.companyCount = result.data.length;
                this.companyList = result.data;
            }
            catch(err) {
                this.loading = false;
                util.Debug.ralert('获取公司失败');
            }
        },
        async discardChanges() {
            await util.MessageBox.ComfirmAsync(this, "确认放弃更改?");
            this.editMode=false;
        },
        selectCompany(i) {
            console.log('[select]', i);
            this.selectedCompany = i;
        },

        // click cell group
        cellGroupClick(name) {
            const handlers = {
                edit: () => { this.editMode = true; },
                newcompany: () => {
                    this.sholdNewCompanyDialogOpen = true;
                },
                remove: async () => {
                    if (!window.config.P.AdminCustomerRemoval) {
                        return util.MessageBox.Show(this, "该管理员没有权限删除客户");
                    }
                    await util.MessageBox.ComfirmAsync(this, "确定要删除该客户吗？");
                    console.log('13234');
                    try {
                        await API.Customer.deleteUser(this.info.uid);
                        util.MessageBox.Show(this, "删除成功");
                        this.$emit('on-request-update-list', "删除成功");
                        this.$emit('on-back-to-user-list', "ok");
                    }
                    catch (err) {
                        console.log(err);
                        util.MessageBox.Show(this, "删除失败, " + err.message);
                    }
                },
                charge: () => {
                    if (!window.config.P.ChargeForCustomer) {
                        return util.MessageBox.Show(this, "该管理员没有权限为客户充值");
                    }
                    this.chargeDialogShouldShow = true;

                },
                deduction: () => {
                    if (!window.config.P.ReceiptForCustomer) {
                        return util.MessageBox.Show(this, "该管理员没有权限为客户扣除余额");
                    }
                    this.shouldDeductionDialodOpen = true;
                },
                aid: () => {
                    this.loadGroups();
                    this.aidDialogShouldShow = true;
                    this.$emit('on-request-update-list', "genxin成功");
                },
            };
            handlers[name]();
        },

        async modifyaid() {
            if (this.selectedAid === -1) {
                return util.MessageBox.Show(this, "请选择一个管理员");
            }

            try {
                await  API.Customer.modifyAid(this.cusData.uid, this.selectedAid)
                alert('修改成功');
            }
            catch (e) {
                console.error(e);
                alert(e.message);
            }

        },

        async requestCheck(action) {
            try {
                await API.Customer.check(this.info.uid, action);
                util.MessageBox.Show(this, "操作成功");
                this.info.checked = this.infoSave.checked = ({
                    accept: 1, reject: 2, resubmit: 0
                })[action];
            }
            catch (e) {
                console.error(e);
                util.MessageBox.Show(this, "操作失败, " + e.message);
            }
        },

        async manualCharge() {
            if (!window.config.P.ChargeForCustomer) {
                this.chargeDialogShouldShow = !true;
                return util.MessageBox.Show(this, "该管理员没有权限为客户充值");
            }
            let I = this.chargeInfo;
            if (isNaN(Number(I.amount)) || I.amount <= 0) {
                return util.MessageBox.Show(this,'请输入金额');
            }
            try {
                this.loading = true;
                let dst = ['pack-balance','tax-balance','other-balance'][this.chargeInfo.type];
                await API.Customer.charge(this.cusData.uid, dst, { amount: this.chargeInfo.amount });
                util.MessageBox.Show(this,'操作成功');
                this.chargeDialogShouldShow = false;
                this.chargeInfo = {
                    type: 0,
                    amount: 0,
                };
                this.loading = !true;
                this.getBasicInfo();
            }
            catch(e) {
                console.error(e);
                this.loading = !true;
                util.MessageBox.Show(this,'操作失败,' + e.message);
            }
        },

        async manualDeduce() {
            if (!window.config.P.ReceiptForCustomer) {
                this.shouldDeductionDialodOpen = !true;
                return util.MessageBox.Show(this, "该管理员没有权限为客户扣除余额");
            }
            let I = this.deduceInfo;
            if (I.cid === -1) {
                return util.MessageBox.Show(this,'请选择公司');
            }
            if (isNaN(Number(I.amount)) || I.amount <= 0) {
                return util.MessageBox.Show(this,'请输入金额');
            }
            console.log(I.credit);
            if (I.credit.data)
                I.credit = I.credit.data;

            //debugger;
            try {
                this.loading = true;
                let dst = ['pack-balance','tax-balance','other-balance'][this.deduceInfo.type];
                await API.Customer.deduction(this.cusData.uid, dst, util.forGetParams(I) );
                util.MessageBox.Show(this,'操作成功');
                this.shouldDeductionDialodOpen = !true;
                this.deduceInfo = {
                    type: 0,
                    amount: 0,
                    credit: '',
                    cid: -1,
                    deduced: false,
                };
                this.loading = false;
                this.getBasicInfo();
            }
            catch(e) {
                console.error(e);
                this.loading = false;
                util.MessageBox.Show(this,'操作失败,' + e.message);
            }
        },
        async loadGroups() {
            this.groups = await API.Group.getSimplifiedList();
        },
        async loadAdmins() {
            this.admins = await API.Group.getUserOfGroup(this.selectedGid);
        }
    },
    watch: {
        editMode(val) {
            this.$emit('on-edit-mode-change', val);
        },
        selectedCompany(val) {
            console.log('[on select-company]', 'ID='+this.companyList[val].id)
            this.$emit('on-select-company', this.companyList[val]);
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
    },
    computed: {
            // 更改前的HASH值
            hashBeforeModify() {
                return md5(util.forGetParams(this.info));
            },
            // 更改后的HASH值
            hashAfterModify() {
                return md5(util.forGetParams(this.infoSave));
            },
            // 城市地址
            area: {
                get() {
                    return [ this.infoSave.province, this.infoSave.city, this.infoSave.district ];
                },
                set(val) {
                    [ this.infoSave.province, this.infoSave.city, this.infoSave.district ] = val;
                }
            },
            formatNumber () {
                if (this.chargeInfo.amount === '') return '0';
                function parseNumber(str) {
                    return str.split(/(?=(\d{3})+$)/g).filter((e,i)=>i%2==0).join(',');
                }
                let [str, prec] = (''+this.chargeInfo.amount).split('.');
                return parseNumber(str) + (prec ? '.'+prec : '');
            },
            formatNumber1 () {
                if (this.deduceInfo.amount === '') return '0';
                function parseNumber(str) {
                    return str.split(/(?=(\d{3})+$)/g).filter((e,i)=>i%2==0).join(',');
                }
                let [str, prec] = (''+this.deduceInfo.amount).split('.');
                return parseNumber(str) + (prec ? '.'+prec : '');
            },
        },
    created() {
        this.info = this.cusData;
        //this.getBasicInfo();
        this.getCompany();
    }
}
</script>

<style>
.float-left {
    float: left;
}

.avatar-mask {
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100px;
    height: 100px;
    border-radius: 100%;
    transition: opacity 0.3s;
    opacity: 0;
    text-align: center;
    cursor: pointer;
    background: rgba(0,0,0,0.3);
    line-height: 100px;
    color: #fff;
}

.avatar-mask:hover {
    opacity: 1;
}

.avatar-mask:active {
    background: rgba(0,0,0,0.5);
}
</style>
