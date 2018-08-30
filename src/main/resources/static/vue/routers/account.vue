<template>
    <div>

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

        <!-- 在线充值 -->
        <Card class="card-margin">
            <Divider orientation="left"><h3>在线充值</h3></Divider>
            
            <Alert type="success"><span style="font-weight: blod; color: green">温馨提示：</span> 微信、支付宝充值即时到账。</Alert>

            <div style="margin-top: 20px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    充值金额
                </span> 
                <Input size="middle" placeholder="" v-model="onlinePayAmount" style="width: 200px; font-size: 14px" />
                <p>
                    <span style="font-size: 16px; width: 100px; display:inline-block"></span>
                    * 请输入100到1,000,000之间的整数。
                </p>
                
            </div>

            <div style="margin-top: 5px; margin-left: 30px;">
                <span style="font-size: 14px; width: 100px; display:inline-block">
                    支付方式
                </span> 
                <RadioGroup v-model="onlinePayMethod" type="button">
                    <Radio label="微信支付" value="0"></Radio>
                    <Radio label="支付宝" value="1"></Radio>
                </RadioGroup>
                
                
            </div>

            <div style="margin-top: 5px; margin-left: 30px;">
                <Checkbox v-model="onlinePayReadTerms">我已阅读<a href="javascript:void(0)">《服务条款》</a></Checkbox>。
                
            </div>

            <div class="inline-margin">
                <Button type="primary" size="middle" style="width: 200px;" shape="circle" icon="ios-card" :disabled="!onlinePayReadTerms" @click="onlinePaymentCharge">充值</Button>
            </div>
        </Card>

        <Card class="card-margin">
            <Divider orientation="left"><h3>对公充值</h3></Divider>
            <Alert type="success">
                <span style="font-weight: blod; color: green">温馨提示：</span> 
                <br/><br/>
                1. 对公转账需我司财务确认后到账，请在此新增线下充值订单并上传支付凭证；<br />
                2. 我司银行账号信息如下：
                <p v-for="e in publicBankAccount">
                    <span style="display:inline-block; min-width:90px; margin-right: 10px">
                        {{ e.recommend ? '<推荐>':'' }}
                    </span>
                    开户名称：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.accountName }}
                    </span>
                    开户银行：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.bankName }}
                    </span>
                    银行账号：<span style="display:inline-block; min-width:100px; margin-right: 10px">
                        {{ e.accountNo }}
                    </span>
                
                    
                </p>
            </Alert>

            <Tabs :value="publicFilterType">
                <TabPane label="全部" name="-1"></TabPane>
                <TabPane label="已确认" name="0"></TabPane>
                <TabPane label="待确认" name="1"></TabPane>
                <TabPane label="已取消" name="2"></TabPane>
            </Tabs>

            <Table border :columns="publicTransferColumnName" :data="publicChargeData"></Table>
            
            <Page :total="publicChargeDataCount" show-sizer show-elevator @on-change="" @on-page-size-change="" />
        </Card>

    </div>
</template>

<script>
import { industry, memberType, paymentMethod, publicOrderStatus } from '../../constant.js';
import '../../css/style.less';
//alert(memberType);

let publicChargeData = [
            {
                id: 1234,
                type: 4,
                amount: 12345.00,
                status: 0,
                tmCreate: '2018/8/31 1:35:00',
                tmConfirm: '',
            },
            {
                id: 1235,
                type: 4,
                amount: 12345.00,
                status: 0,
                tmCreate: '2018/8/31 1:35:00',
                tmConfirm: '',
            },
        ]

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
        onlinePayMethod: 0,
        onlinePayAmount: "", // :String
        onlinePayReadTerms: false,
        publicBankAccount: [ // 从配置文件获取
            {
                recommend: true,
                accountName: 'xxxx公司',
                bankName: 'xxxx银行',
                accountNo: '10030010000'
            },
            {
                recommend: false,
                accountName: 'xxxx公司',
                bankName: 'xxxxxx银行',
                accountNo: '20030010000'
            },
        ],
        publicFilterType: 0,
        publicTransferColumnName: [
            { type: 'selection', width: 60, align: 'center' },
            { title: '序号', type: 'index' },
            { title: '订单编号', key: 'id' },
            { title: '订单类型', render: (h, params) => { console.log('h', this); return h('span', {}, paymentMethod[publicChargeData[params.index].type]) } },
            { title: '订单金额', key: 'amount' },
            { title: '订单状态', render: (h, params) => h('span', {}, publicOrderStatus[publicChargeData[params.index].status]) },
            { title: '下单时间', key: 'tmCreate' },
            { title: '确认时间', key: 'tmConfirm' },
            { 
                title: '操作', 
                key: 'action', 
                render: (h, params) => {
                    return h('div', [
                        h('a', {
                            props: {
                                href: 'javascript:void(0)',
                            },
                            on: {
                                click() {
                                    console.log('click', this);
                                    console.log('Cancel index: ' + params.index);
                                }
                            }
                        }, '取消订单'),
                        h('span', {}, ' | '),
                        h('a', {
                            props: {
                                href: 'javascript:void(0)',
                            },
                            on: {
                                click() {
                                    console.log('View index: ' + params.index);
                                }
                            }
                        }, '查看详情')
                    ]);
                }
            }
        ],
        publicChargeData,
        publicChargeDataCount: 200,

        
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
        // 在线充值
        onlinePaymentCharge() {
            //console.log(Vue, $);
            let amount = Number(this.onlinePayAmount);
            if (this.onlinePayAmount === "" || Number.isNaN(amount)) {
                alert('请输入数字');
                return;
            }
            alert('/// TODO: Onlien charge')
        }
    },
    created() {
        console.log(this.publicChargeDataCount);
    }
}
</script>

<style>

</style>