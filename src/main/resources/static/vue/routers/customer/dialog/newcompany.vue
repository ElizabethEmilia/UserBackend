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
                        <FormItem label="公司名称" label-position="top">
                            <Input v-model="formData.lpname" placeholder="" />
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="法人名称" label-position="top">
                            <Input v-model="formData.name" placeholder="" />
                        </FormItem>
                    </Col>
                </Row>
                <Row :gutter="32">
                    <Col span="12">
                        <FormItem label="征税类型" label-position="top">
                            <Select v-model="formData.taxType" placeholder="please select an owner">
                                <Option value="jobs">Steven Paul Jobs</Option>
                                <Option value="ive">Sir Jonathan Paul Ive</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="增值纳税人类型" label-position="top">
                            <Select v-model="formData.vatType" placeholder="please choose the type">
                                <Option value="private">Private</Option>
                                <Option value="public">Public</Option>
                            </Select>
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
                            <Select v-model="formData.entOrgType" placeholder="please choose the type">
                                <Option value="private">Private</Option>
                                <Option value="public">Public</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="12">
                        <FormItem label="投资类型" label-position="top">
                            <Input v-model="formData.invType" placeholder="" />
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
                <Button type="primary" @click="drawerVisible = false">添加公司</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
    import Init from '../../../../js/init.js';

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