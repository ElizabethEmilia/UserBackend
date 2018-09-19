<template>
    <Card class="card-margin">
        <p slot="title">
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
                <Row >
                    <Divider dashed orientation="right" ><a href="javascript:void(0)" :disabled="true">编辑</a></Divider>
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
                <Row v-if="false">
                    <Divider dashed orientation="right" >
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
                            <span class="title-before-input wider"> <i class="required" />征税类型 </span>
                            <Select v-model="infoSave.taxType" style="width:200px">
                                <Option v-for="(e,i) in [1,2,3,4,5,6]" :value="i" :key="i">{{ e }}</Option>
                            </Select>
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />增值纳税人类型 </span>
                            <Select v-model="infoSave.vatType" style="width:200px">
                                <Option v-for="(e,i) in [1,2,3,4,5,6]" :value="i" :key="i">{{ e }}</Option>
                            </Select>
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />增值税税率 </span>
                            <Input v-model="infoSave.vaTaxRatio" placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />增值税报税频率 </span>
                            <Input v-model="infoSave.vatrFreq" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />城建税 </span>
                            <Input v-model="infoSave.cbTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />教育费附加 </span>
                            <Input v-model="infoSave.eaTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />地方教育费附加 </span>
                            <Input v-model="infoSave.leaTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />河道费 </span>
                            <Input v-model="infoSave.riverTax" clearable placeholder="" style="width: 200px" />
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />企业组织类型 </span>
                            <!--Select v-model="infoSave.entOrgType" style="width:200px">
                                <Option v-for="(e,i) in enterpriseOrgizationTypes" :value="i" :key="i">{{ e }}</Option>
                            </Select-->
                            个人独资企业
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />投资类型 </span>
                            <!--Input v-model="infoSave.invType" clearable placeholder="" style="width: 200px" /-->
                            个人独资
                        </div>

                        <div style="margin-bottom: 5px;">
                            <span class="title-before-input wider"> <i class="required" />主营业务类别 </span>
                            <Input type="textarea" v-model="infoSave.businessType" placeholder="" style="width: 200px" />
                        </div>

                    </Card>
                </Row>
            </Col>
            <Col span="12">

                <Row>
                    <!--Card  :bordered="false" dis-hover>
                        <p slot="title">操作</p>

                        <CellGroup>
                            <Cell name="mamage" title="公司设立进度" />
                            <Cell name="mamage" title="公司证照" />
                        </CellGroup>

                    </Card-->
                </Row>
            </Col>
        </Row>
    </Card>
</template>

<script>

    import { enterpriseOrgizationTypes, industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';

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
            selectedCompany: -1,
            avatar: '/images/avatar-company.png',
            info: {},
            industry,
            memberType,
            enterpriseOrgizationTypes,
        }),
        methods: {

        },
        watch: {
            comData(val) {
                this.info = val;
            }
        },
        computed: {

        },
        created() {
            this.info = this.comData;
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
