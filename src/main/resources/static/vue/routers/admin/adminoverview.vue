<template>
    <!-- 账号总览 -->
    <Card class="card-margin">
        <Divider orientation="left"><h3>账号总览</h3></Divider>
        <Row>
            <Col span="12">
                <Row>
                    <div :style="{ backgroundImage: 'url(/res/avatar/admin.png)' }" class="avatar float-left">
                        <div style="position: relative;" v-if="false">
                            <div :style="pendingUpload?{ opacity: 1 }:{}" class="avatar-mask" @click="chooseFile">
                                {{ pendingUpload ? '上传中' : '更换头像' }}
                                <input ref="openFile" @change="uploadFile" type="file" style="display:none" />
                            </div>
                        </div>
                    </div>
                    <div class="float-left" style="margin-left: 20px;">
                        <span style="font-size: 16px">管理员 / </span>
                        <span style="font-size: 20px; color: #000">{{ info.name }}</span>
                    </div>
                </Row>
                <Row v-if="!editMode">
                    <Divider dashed orientation="right" ><a href="javascript:void(0)" @click="edit_basicInformation()">编辑</a></Divider>
                    <!-- 用户信息显示开始 -->

                    <Row class="line-margin">
                        <Col span="12"><Icon type="ios-call" /> {{ info.phone }}</Col>
                        <Col span="12"><Icon type="ios-mail" /> 超级管理员（确认类型）  </Col>
                    </Row>
                    <!-- 用户信息显示结束 -->
                </Row>
                <Row v-else>
                    <Divider dashed orientation="right" >
                        <a v-if="hashBeforeModify != hashAfterModify" @click="discardChanges()" href="javascript:void(0)" style="margin-right: 10px;">放弃更改</a>
                        <a href="javascript:void(0)" v-if="hashAfterModify == hashBeforeModify" @click="editMode=false">返回</a>
                        <a v-else :disabled="pendingSave" href="javascript:void(0)" @click="saveChanges_basicInformation">{{ pendingSave?'正在':'' }}保存</a>
                    </Divider>
                    <Card :bordered="false" dis-hover>
                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input"> <i class="required" />名称 </span>
                            <Input v-model="infoSave.name" placeholder="" clearable style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input"> <i class="required" />手机号 </span>
                            <Input v-model="infoSave.phone" disabled placeholder="" style="width: 200px" />
                        </div>


                    </Card>
                </Row>
            </Col>
            <Col span="12">

                <Row>
                    <ModifyPassword v-if="editMode" />
                </Row>
            </Col>
        </Row>
    </Card>
</template>

<script>

    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import util from '../../../js/util.js';
    import $ from '../../../js/ajax.js';
    import md5 from 'js-md5';
    import ModifyPassword from './adminmodifypwd.vue';

    /**
     * 事件
     *
     * on-edit-mode-change:
     */

    export default {
        components: {
            ModifyPassword
        },
        data: () => ({
            info: {
                uid: 0,
                lid: -1,
                name: 'Miyuki',
                privilege: 1,
                phone: '110',
            },
            infoSave: {},
            companyCount: 2,
            editMode: false,

            avatar: null,
            industry,
            memberType,

            pendingSave: false, //记录是否在保存
            pendingUpload: false, //是否正在上传头像
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
                if (d.name == '') e.push('名称');
                if (e.length != 0) {
                    alert(e.join('、')+'不能为空');
                    return;
                }

                this.pendingSave = true;
                try {
                    let result = await $.ajax('/api/admin/basic', this.infoSave);
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
                    let result = await $.ajax('/api/admin/basic');
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
            async discardChanges() {
                await util.MessageBox.ComfirmAsync(this, "确认放弃更改?");
                this.editMode=false;
            }
        },
        watch: {
            editMode(val) {
                this.$emit('on-edit-mode-change', val);
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
            }
        },
        created() {
            this.getBasicInfo();
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
