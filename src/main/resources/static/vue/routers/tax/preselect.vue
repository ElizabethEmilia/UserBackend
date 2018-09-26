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
            <Col span="8">{{ infoSave.ysaRange }}</Col>
            <Col span="4">当前税金预交率</Col>
            <Col span="8">{{ infoSave.preTaxRatio }}</Col>
        </Row>

        <Divider />
        请选择预交年销售额范围:
        <Select v-model="selected">
            <Option v-for="(e, i) in range" :key="i" :value="1 << (1 + i)">{{ e }}</Option>
        </Select>
        <div v-if="note[selected]">
            <p>
                税金预交率：{{ ['一档', '二挡'][selected] }}({{ note[selected].ratio }}%)
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
    import { Integers } from '../../../constant.js';

    export default {
        props: [ 'obj' ],
        data: () => ({
            companyInfo: init.tCompany,
            infoSave: null,

            range: [ "(一档/3%)小于等于360,000", "(二档/6%)360,000~1,000,000" ],
            selected: 0,
            note: [
                {
                    note: "月销售额小于等于3万或季销售额小于等于9万，没有开具增值税专用发票需求的客户。",
                    ratio: 3,
                },
                {
                    note: "预计年销售额大于36万且小于等于100万，或预计年销售额小于等于36万但月销售额大于3万或季度销售额大于9万的客户。",
                    ratio: 6
                }
            ],
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
                this.$emit('on-change', val);
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