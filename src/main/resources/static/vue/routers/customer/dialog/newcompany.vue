<template>
    <div>
        <Modal
                title="Create"
                v-model="drawerVisible"
                width="720"
                style="margin-top: 10px;"
        >
            <Form>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="* 公司名称" label-position="top">
                            <Input v-model="formData.name" placeholder="" />
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="* 法人名称" label-position="top">
                            <Input v-model="formData.lpname" placeholder="" />
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="征税类型" label-position="top">
                            <Input v-model="formData.taxType" placeholder="" />

                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="增值纳税人类型" label-position="top">
                            <Input v-model="formData.vatType" placeholder="" />
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="增值税税率" label-position="top">
                            <Input v-model="formData.vaTaxRatio" placeholder="" />
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="增值税报税频率" label-position="top">
                            <Input v-model="formData.vatrFreq" placeholder="" />
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="企业组织类型" label-position="top">
                            个人独资企业
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="投资类型" label-position="top">
                            个人独资
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="主营业务类别" label-position="top">
                            <Input v-model="formData.businessType" placeholder="" />
                        </FormItem>
                    </Col>
                    <Col span="12">

                    </Col>
                </Row>

            </Form>
            <div class="demo-drawer-footer">
                <Button style="margin-right: 8px" @click="drawerVisible = false">取消</Button>
                <Button type="primary" @click="add">添加公司</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
    import Init from '../../../../js/init.js';
    import util from '../../../../js/util.js';
    import API from '../../../../js/api.js';

    export default {
        props: [ 'value' ],
        data () {
            return {
                drawerVisible: false,
                styles: {
                    height: 'calc(100% - 55px)',
                    overflow: 'auto',
                    paddingBottom: '53px',
                    position: 'static'
                },
                formData: Init.tCompany,
            }
        },
        watch: {
            drawerVisible(val) {
                this.$emit('input', val);
            },
            value(val) {
                this.drawerVisible = val;
                this.formData.m
            }
        },
        mounted() {
            this.drawerVisible = this.value;
        },
        methods: {
            async add() {
                let I = this.formData;

                let name = I.name;
                let lpname = I.lpname;
                if (util.String.isNullOrEmpty(name) || util.String.isNullOrEmpty(lpname)) {
                    return util.MessageBox.Show(this, "请填写公司名称和法人名称");
                }

                try {
                    await API.Customer.Company.newCompany(this.$parent.cusData.uid, I);
                    util.MessageBox.Show(this, "操作成功");
                    this.$parent.getCompany();
                    this.drawerVisible = false;
                }
                catch (e) {
                    util.MessageBox.Show(this, "操作失败:" + e.msg);
                    console.error(e);
                }

            }
        }
    }
</script>
<style>
    .demo-drawer-footer{
        width: 100%;
        position: absolute;
        bottom: 0;
        left: 0;
        border-top: 1px solid #e8e8e8;
        padding: 10px 16px;
        text-align: right;
        background: #fff;
    }
</style>