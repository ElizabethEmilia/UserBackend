<template>
    <Card class="card-margin">
        <p slot="title">
            <a  @click="$emit('on-back-to-user-list');" href="javascript:void(0)" style="margin-right: 10px;">返回</a>
            {{ info.name }}
        </p>
        <!-- 信息总览 -->
        <Row>
            <Col span="12">
                <Row>
                    <div :style="avatar ? { backgroundImage: 'url(' + avatar + ')' }:{}" class="avatar float-left">
                    </div>
                    <div class="float-left" style="margin-left: 20px;">
                        <span style="font-size: 20px; color: #000">{{ info.name }}</span>
                        <p> {{ info.entOrgType }} </p>
                    </div>
                </Row>
                <Row v-if="!editMode">
                    <Divider dashed orientation="right" ><a href="javascript:void(0)" @click="edit_basicInformation()">编辑</a></Divider>
                    <!-- 用户信息显示开始 -->

                    <Row class="line-margin">
                        <Col span="6">公司名称</Col>
                        <Col span="18">{{ info.name }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">法人姓名</Col>
                        <Col span="18">{{ info.lpname }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">征税类型</Col>
                        <Col span="18">{{ info.taxType }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">增值纳税人类型</Col>
                        <Col span="18">{{ info.vatType }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">预计年销售额范围</Col>
                        <Col span="18">{{ info.ysaRange }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">税金预交率</Col>
                        <Col span="18">{{ info.preTaxRatio }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">增值税税率</Col>
                        <Col span="18">{{ info.vaTaxRatio }}</Col>
                    </Row>
                    <Row class="line-margin">
                        <Col span="6">企业组织类型</Col>
                        <Col span="18">个人独资企业</Col>
                    </Row>


                    <!-- 用户信息显示结束 -->
                </Row>
                <Row v-else>
                    <Divider dashed orientation="right" >
                        <a v-if="hashBeforeModify != hashAfterModify" @click="discardChanges()" href="javascript:void(0)" style="margin-right: 10px;">放弃更改</a>
                        <a href="javascript:void(0)" v-if="hashAfterModify == hashBeforeModify" @click="editMode=false">返回</a>
                        <a v-else :disabled="pendingSave || !P.AdminCompanyAddAndModify" href="javascript:void(0)" @click="saveChanges_basicInformation">{{ pendingSave?'正在':'' }}保存</a>
                    </Divider>
                    <Card :bordered="false" dis-hover>
                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />公司名称 </span>
                            <Input v-model="infoSave.name" placeholder="" clearable style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />法人姓名 </span>
                            <Input v-model="infoSave.lpname" placeholder="" clearable style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">征税类型 </span>
                            <Select v-model="infoSave.taxType" style="width:200px">
                                <Option v-for="(e,i) in [1,2,3,4,5,6]" :value="i" :key="i">{{ e }}</Option>
                            </Select>
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">增值纳税人类型 </span>
                            <Select v-model="infoSave.vatType" style="width:200px">
                                <Option v-for="(e,i) in [1,2,3,4,5,6]" :value="i" :key="i">{{ e }}</Option>
                            </Select>
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">增值税税率 </span>
                            <Input v-model="infoSave.vaTaxRatio" placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">增值税报税频率 </span>
                            <Input v-model="infoSave.vatrFreq" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">城建税 </span>
                            <Input v-model="infoSave.cbTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">教育费附加 </span>
                            <Input v-model="infoSave.eaTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">地方教育费附加 </span>
                            <Input v-model="infoSave.leaTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">河道费 </span>
                            <Input v-model="infoSave.riverTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">企业组织类型 </span>
                            <!--Select v-model="infoSave.entOrgType" style="width:200px">
                                <Option v-for="(e,i) in enterpriseOrgizationTypes" :value="i" :key="i">{{ e }}</Option>
                            </Select-->
                            个人独资企业
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">投资类型 </span>
                            个人独资
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider">主营业务类别 </span>
                            <Input type="textarea" v-model="infoSave.businessType" placeholder="" style="width: 200px" />
                        </div>

                    </Card>
                </Row>
            </Col>
            <Col span="12">

                <Row>
                    <Card  :bordered="false" dis-hover>
                        <p slot="title">操作</p>

                        <CellGroup>
                            <Cell v-if="P.AdminCompanyAddAndModify" name="edit" title="编辑公司资料" />
                            <Cell name="mamage" title="公司设立进度" />
                            <Cell name="mamage" title="公司证照" />
                            <Cell v-if="P.AdminCompanyRemoval" name="delete" title="删除公司" style="color: red"/>
                        </CellGroup>

                    </Card>
                </Row>
            </Col>
        </Row>
    </Card>
</template>

<script>

    import { enterpriseOrgizationTypes, industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import util from '../../../js/util.js';
    import $ from '../../../js/ajax.js';
    import md5 from 'js-md5';

    /**
     * 事件
     *
     * on-edit-mode-change:
     * on-select-company:   选择公司的时候触发
     */

    export default {
        props: [ 'cusData', 'comData' ],
        data: () => ({
            infoSave: {},
            companyCount: 0,
            companyList: [],
            editMode: false,
            selectedCompany: -1,
            stats: {
                income: 1000,
                lastIncome: 1000,
                outcome: 200,
                lastOutcome: 20,
                balance: 1000000,
            },
            avatar: '/images/avatar-company.png',
            industry,
            memberType,
            enterpriseOrgizationTypes,
            provinces: ['四川省'],
            cities: ['攀枝花市'],
            district: ['东区', '西区'],
            pendingSave: false, //记录是否在保存
            pendingUpload: false, //是否正在上传头像
            P: window.config.P,
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
                    let result = await $.ajax('/api/account/basic', this.infoSave);
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
                    alert('保存失败');
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
                    alert('上传头像失败');
                }
                this.pendingUpload = false;
            },
            // 获取用户基本信息
            async getBasicInfo() {
                try {
                    let result = await $.ajax('/api/account/basic');
                    if (result.code) {
                        return alert('获取基本信息失败：' + result.msg);
                    }
                    this.info = result.data;
                    this.avatar = result.data.avatar;
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
                    let result = await $.ajax(`/api/customer/${ this.cusData.uid }/company/list`);
                    if (result.code) {
                        return alert('获取公司失败：' + result.msg);
                    }
                    this.companyCount = result.data.length;
                    this.companyList = result.data;
                }
                catch(err) {
                    util.Debug.ralert('获取公司失败');
                }
            },
            async discardChanges() {
                await util.MessageBox.ComfirmAsync(this, "确认放弃更改?");
                this.editMode=false;
            },
        },
        watch: {
            editMode(val) {
                this.$emit('on-edit-mode-change', val);
            },
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
            info() {
                return this.comData;
            }
        },
        created() {
            this.info = this.cusData;
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

    .wider { width: 150px; }
</style>
