<template>
    <!-- 账号总览 -->
    <Card class="card-margin">
            <Divider orientation="left"><h3>账号总览</h3></Divider>
            <Row>
                <Col span="12">
                    <Row>
                        <div :style="avatar ? { backgroundImage: 'url(' + avatarURI + ')' }:{}" class="avatar float-left">
                            <div style="position: relative;" v-if="editMode">
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
                    <Row v-show="!editMode">
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
                    <Row v-show="editMode">
                        <Divider dashed orientation="right" >
                            <a v-if="hashBeforeModify != hashAfterModify" @click="discardChanges()" href="javascript:void(0)" style="margin-right: 10px;">放弃更改</a>
                            <a href="javascript:void(0)" v-if="hashAfterModify == hashBeforeModify" @click="editMode=false">返回</a>
                            <a v-else :disabled="pendingSave" href="javascript:void(0)" @click="saveChanges_basicInformation">{{ pendingSave?'正在':'' }}保存</a>
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


                        </Card>
                    </Row>
                </Col>
                <Col span="12">
                    <Card :bordered="false" dis-hover>
                        <p slot="title">我的公司</p>
                        <P>
                            我有 {{ companyCount }} 家公司。
                        </p>
                    </Card>
                    
                    <Row>
                        <Card  :bordered="false" dis-hover  v-show="!editMode">
                            <p slot="title">我的钱包</p>
                            <Row>
                                <Col span="12" style="text-align: center; padding-top: 13px;">
                                    <Card :bordered="false" dis-hover>
                                        
                                        余额：<span style="font-size: 25px; color: green">{{ loadingBanlance ? "--" : Number((info.packBalance) + (info.taxBalance) + (stats.otherBalance)).toFixed(2) }}</span>
                                        <Button type="success" style="font-size: 17px; width: 100%;  margin-top: 15px;">充值</Button>
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
                                            <Col span="12">余额详情</Col>
                                            <Col span="12" style="text-align: right"></Col>
                                        </Row>
                                        <Divider dashed ></Divider>
                                        <Row>
                                            <Col span="12">年费余额</Col>
                                            <Col span="12" style="text-align: right">{{ Number(info.packBalance).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">税金余额</Col>
                                            <Col span="12" style="text-align: right">{{ Number(info.taxBalance).toFixed(2) }}</Col>
                                        </Row>
                                        <Row>
                                            <Col span="12">其他余额</Col>
                                            <Col span="12" style="text-align: right">{{ Number(stats.otherBalance).toFixed(2) }}</Col>
                                        </Row>
                                    </Card>
                                </Col>
                            </Row>

                        </Card>
                    </Row>

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
import init from '../../../js/init.js';
import $ from '../../../js/ajax.js';
import md5 from 'js-md5';
import ModifyPassword from './modifyPassword.vue';
import SelectArea from '../../components/areaselect.vue';

/**
 * 事件
 * 
 * on-edit-mode-change: 
 */

export default {
    components: {
        ModifyPassword, SelectArea,
    },
    data: () => ({
        info: init.tCustomer,
        infoSave: {},
        companyCount: 0,
        editMode: false,
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
        pendingSave: false, //记录是否在保存
        pendingUpload: false, //是否正在上传头像
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
        async getBasicInfo(callback) {
            this.loadingBanlance = true;
            try {
                let result = await $.ajax('/api/account/basic');
                if (result.code) {
                    return alert('获取基本信息失败：' + result.msg);
                }
                this.info = result.data;
                this.avatar = result.data.avatar;
                typeof callback === "function" && callback(result);
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
        // 获取收入信息
        async getRecentExchange() {
            try {
                let result = await $.ajax('/api/exchange/recent');
                if (result.code) {
                    return alert('获取收入信息失败：' + result.msg);
                }
                this.stats = result.data;
            }
            catch(err) {
                 util.Debug.ralert('获取收入信息失败');
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

            avatarURI() {
                let avatar = this.avatar;
                if (avatar.indexOf("data:") === 0)
                    return avatar;
                else
                    return "/res/avatar/" + avatar;
            }
    },
    created() {
        this.getBasicInfo(async (result) => {
            // NOTE: 付费用户直接看不道这个页面了
            //await util.MessageBox.Show(this, "您还不是付费用户，即将前往产品介绍页面");
            //location.href='#';
            // TODO: 对于快到期的用户  需要判断什么时候到期并显示
        });
        this.getRecentExchange();
        this.getCompanyCount();
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
