<template>
    <div class="ft13">
        <Row>
            <Col span="4">公司名称</Col>
            <Col span="8">{{ infoSave.name }}</Col>
            <Col span="4"></Col>
            <Col span="8"></Col>
        </Row>
        <Row>
            <Col span="4">年销售额范围</Col>
            <Col span="8">{{ ysaRange[infoSave.ysaRange] }}</Col>
            <Col span="4">当前税金预交率</Col>
            <Col span="8">{{ infoSave.preTaxRatio }}</Col>
        </Row>

        <Divider />
        请选择预交年销售额范围:
        <Select v-model="selected">
            <Option v-for="(e, i) in range" :key="i" :value="i">{{ e }}</Option>
        </Select>
        <div v-if="note[selected]">
            <p>
                税金预交率：{{ ['一档', '二挡', '三档'][selected] }}({{ note[selected].ratio }}%)
            </p>
            <p>
                说明：适用于{{ note[selected].note }}
            </p>
            <p>
                选择此档后，您在每次开票时，会预交{{ note[selected].ratio }}%的综合税金，用于缴纳应交的的增值税、附加税、个人所得税等。
            </p>
        </div>

    </div>
</template>

<script>
    import init from '../../../js/init.js';
    import { ysaRange, Integers } from '../../../constant.js';

    export default {
        props: [ 'obj' ],
        data: () => ({
            isAdmin: window.config.isAdmin,
            P: window.config.P,

            ysaRange,
            companyInfo: init.tCompany,
            infoSave: null,

            range: (function() {
                if (window.config.isAdmin && window.config.P.CanGiveHigherRangeOfExpectedIncome) {
                    return [ "(一档/3%)小于等于360,000", "(二档/6%)360,000~1,000,000", "(三档/6%)1,000,000-5,000,000" ];
                }
                return [ "(一档/3%)小于等于360,000", "(二档/6%)360,000~1,000,000" ];
            })(),
            selected: -1,
            note: (function() {
                if (window.config.isAdmin && window.config.P.CanGiveHigherRangeOfExpectedIncome) {
                    return [
                        {
                            note: "月销售额小于等于3万或季销售额小于等于9万，没有开具增值税专用发票需求的客户。",
                            ratio: 3,
                        },
                        {
                            note: "预计年销售额大于36万且小于等于100万，或预计年销售额小于等于36万但月销售额大于3万或季度销售额大于9万的客户。",
                            ratio: 6
                        }, {
                            note: "预计年销售额大于100万且小于等于500万的客户。",
                            ratio: 7
                        },

                    ];
                }
                return [
                    {
                        note: "月销售额小于等于3万或季销售额小于等于9万，没有开具增值税专用发票需求的客户。",
                        ratio: 3,
                    },
                    {
                        note: "预计年销售额大于36万且小于等于100万，或预计年销售额小于等于36万但月销售额大于3万或季度销售额大于9万的客户。",
                        ratio: 6
                    },
                ];
            })(),
            selectedValue: [ Integers.SallyRange.LESS_THAN_360K, Integers.SallyRange.BETWEEN_360K_AND_1M ],
        }),
        watch: {
            obj(val) {
                this.companyInfo = val ? val : this.companyInfo;
            },
            companyInfo(val) {
                this.selected = val.preTaxRatio;
            },
            selected(val) {
                debugger;
                this.$emit('on-change', 1 << (1 + val));
            },
        },
        created() {
            this.companyInfo = this.obj ? this.obj : this.companyInfo;
            this.selected = this.companyInfo.preTaxRatio;
            this.infoSave = this.companyInfo;
        }
    }
</script>

<style scoped>
.w100 { display: inline-block; width: 100px; }
.w200 { display: inline-block; width: 200px; }
</style>