<template>
    <!-- 账号总览 -->
    <Card class="card-margin">
            <Divider orientation="left"><h3>账号总览</h3></Divider>
            <Row>
                <Col span="12">
                    <Row>
                        <Col span="10">
                            <div class="avatar"></div>
                        </Col>
                        <Col span="14">

                            <span style="font-size: 16px">{{ memberType[ info.type ] }} / </span>
                            <span style="font-size: 20px; color: #000">{{ info.name }}</span>

                            <p>
                                {{ industry[info.industry] }}
                            </p>
                        </Col>
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
                        <Divider dashed orientation="right" ><a href="javascript:void(0)" @click="saveChanges_basicInformation">保存</a></Divider>
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
                                <Input v-model="infoSave.phone" placeholder="" clearable style="width: 200px" />
                            </div>

                            <div style="margin-bottom: 5px;">
                            <span class="title-before-input">邮箱 </span>
                                <Input v-model="value14" placeholder="" clearable style="width: 200px" />
                            </div>

                            <div style="margin-bottom: 5px;">
                                <span class="title-before-input">微信号 </span>
                                <Input v-model="value14" placeholder="" clearable style="width: 200px" />
                            </div>

                            <div style="margin-bottom: 5px;">
                                <span class="title-before-input">QQ </span>
                                <Input v-model="value14" placeholder="" clearable style="width: 200px" />
                            </div>

                            <div style="margin-bottom: 5px;">
                                <span class="title-before-input">传真 </span>
                                <Input v-model="value14" placeholder="" clearable style="width: 200px" />
                            </div>

                            <div style="margin-bottom: 5px;">
                                <span class="title-before-input">地址 </span>
                                <Select v-model="infoSave.province" style="width: 100px;">
                                    <Option v-for="(e,i) in provinces" :value="e" :key="e">{{ e }}</Option>
                                </Select>
                                <Select v-model="infoSave.city" style="width: 100px;">
                                    <Option v-for="(e,i) in cities" :value="e" :key="e">{{ e }}</Option>
                                </Select>
                                <Select v-model="infoSave.district" style="width: 100px;">
                                    <Option v-for="(e,i) in district" :value="e" :key="e">{{ e }}</Option>
                                </Select>
                                <p style="margin-top: 5px;">
                                    <span class="title-before-input"> </span>
                                    <Input v-model="value14" placeholder="" clearable style="width: 300px" />
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
                        <Card  :bordered="false" dis-hover>
                            <p slot="title">我的钱包</p>
                            <Row>
                                <Col span="12" style="text-align: center; padding-top: 13px;">
                                    <Card :bordered="false" dis-hover>
                                        
                                        余额：<span style="font-size: 25px; color: green">{{ Number(info.balance).toFixed(2) }}</span>
                                        <Button type="success" style="font-size: 17px; width: 100%;  margin-top: 15px;">充值</Button>
                                    </Card>
                                </Col>
                                <Col span="12">
                                    <Card :bordered="false" dis-hover>
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
                                    </Card>
                                </Col>
                            </Row>

                        </Card>
                    </Row>
                </Col>
            </Row>
        </Card>
</template>

<script>

import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
import '../../../css/style.less';


export default {
    data: () => ({
        info: {
            uid: 0,
            lid: -1,
            name: 'Miyuki',
            type: 1,
            industry: 1,
            phone: '110',
            email: 'listencpp@126.com',
            wechat: '',
            qq: '1212434',
            fax: '',
            province: '四川省',
            city: '攀枝花市',
            district: '东区',
            address: '五十四小学',
            avatar: '#eee',
            otherContact: '',
            paid: true,
            balance: 100000.00,
            rec_type: 0,
            reg_date: '1989-6-4'
        },
        infoSave: {},
        companyCount: 2,
        editMode: false,
        stats: {
            income: 1000,
            lastIncome: 1000,
            outcome: 200,
            lastOutcome: 20,
        },
        industry,
        memberType,
        provinces: ['四川省'],
        cities: ['攀枝花市'],
        district: ['东区', '西区'],
    }),
    methods: {
         // 基础信息的编辑
        edit_basicInformation() {
            this.editMode = true
            this.infoSave = Object.assign({}, this.info);
        },
        // 基础信息的保存
        saveChanges_basicInformation() {
            alert('/// TODO: Save changes');
            this.editMode = false;
        },
    }
}
</script>
