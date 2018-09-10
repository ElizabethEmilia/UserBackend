<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>公司证照</h3></Divider>

        <PagedTable v-if="selected != -1" :columns="columns" :data-source="`customer/_/company/${cid}/cert`" />
        <div style="margin-top: 20px;">
            <Button type="success" @click="dialogVisible.newCert = true">上传证照</Button>
        </div>
        <div style="margin-top: 20px;">

        </div>

        <Modal
            :width="800"
            v-model="dialogVisible.newCert"
            title="新增证照"
        >
            <Row>
                <Col span="12">
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />证件编号 </span>
                        <Poptip trigger="focus" >
                            <Input v-model="newCert.certNo" style="width: 200px"  />
                            <div slot="content" style="text-align: center"><span style="font-size: 20px; color: green;">&nbsp;{{ newCert.certNo }}</span></div>
                        </Poptip>
                    </div>
                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />证件名称 </span>
                        <Input v-model="newCert.certName" placeholder="" clearable style="width: 200px" />
                    </div>
                </Col>
                <Col span="12">
                    <UploadFile v-model="newCert.certImg" title="上传公司证照"/>
                </Col>
            </Row>
        </Modal>
    </Card>
</template>

<script>
    import $ from '../../../js/ajax.js';
    import util from '../../../js/util.js';
    import PagedTable from '../../pagedTable.vue';
    import UploadFile from '../../components/uploadfile.vue';

    const certInit = {
        cid: '',
        certNo: '',
        certName: '',
        certImg: '',
    };

    export default {
        props: [ 'cid' ],
        components: {
            PagedTable, UploadFile
        },
        data: () => ({
            newCert: certInit,
            dialogVisible: {
                newCert: false,
            },
            columns() {
                let self = this;
                return [
                    { title: '序号', type: 'index' },
                    { title: '公司ID', key: 'name' },
                    { title: '公司名称', key: 'cid' },
                    { title: '证件编号', key: 'certNo' },
                    { title: '证件名称', key: 'certName' },
                    {
                        title: '操作',
                        key: 'action',
                        render: (h, params) => {
                            console.log(h, params);
                            return h('div', [
                                h('a', {
                                    props: {
                                        href: 'javascript:void(0)',
                                    },
                                    on: {
                                        async click() {
                                            let r = await $.ajax('/api/customer/_/cert/'+self.p(params).id+'/delete');
                                            if (r.code === 0) {
                                                util.MessageBox.Show(this, "删除成功");
                                                return self.refresh();
                                            }
                                            return util.MessageBox.Show(this, "删除失败");
                                        }
                                    }
                                }, '删除'),
                            ]);
                        }
                    },
                ];
            },
        }),
        methods: {
        },
    }
</script>

<style>

</style>
