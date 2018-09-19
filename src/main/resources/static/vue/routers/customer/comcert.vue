<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>公司证照</h3></Divider>

        <PagedTable ref="dt" :columns="columns" :data-source="`customer/_/company/${cid}/cert`" />
        <div style="margin-top: 20px;">
            <Button type="success" @click="dialogVisible.newCert = true">上传证照</Button>
        </div>
        <div style="margin-top: 20px;">

        </div>

        <Modal
            :width="800"
            v-model="dialogVisible.newCert"
            title="新增证照"
            @on-ok="upload"
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
    import API from '../../../js/api.js';
    import util from '../../../js/util.js';
    import PagedTable from '../../pagedTable.vue';
    import UploadFile from '../../components/uploadfile.vue';
    import init from '../../../js/init.js';
    import render from '../../../js/render.js';

    export default {
        props: [ 'cid' ],
        components: {
            PagedTable, UploadFile
        },
        data: () => ({
            newCert: init.tComCert,
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
                        render: (h, p) => {
                            return h('div', [
                                render.link(h, p, '删除', async function() {
                                    await util.MessageBox.ComfirmAsync(this, "确实要删除吗？");
                                        try {
                                            let r = API.Company.Certificates.remove(p.row.id);
                                            util.MessageBox.Show(self, "删除成功");
                                            self.refresh();
                                        }
                                        catch (e) {
                                            console.error(e);
                                            return util.MessageBox.Show(self, "删除失败");
                                        }
                                    }),
                                render.link(h, p, '查看', function() {
                                    window.open('/res/avatar/' + p.row.certImg);
                                })
                            ]);
                        }
                    },
                ];
            },
        }),
        methods: {
            async upload() {
                let C = this.newCert;
                let certName = C.certName, certImg = C.certImg.data, certNo = C.certNo;
                debugger;
                if (util.String.isNullOrEmpty(certName) ||
                        util.String.isNullOrEmpty(certImg) ||
                        util.String.isNullOrEmpty(certNo)
                ) {
                    util.MessageBox.Show(this, "请输入所有必填信息");
                    this.dialogVisible.newCert = true;
                    return;
                }

                // start upload
                try {
                    await API.Company.Certificates.add(this.cid, Object.assign(C, { certImg }));
                    util.MessageBox.Show(this, "添加成功");
                    this.$refs.dt.refresh();
                }
                catch (e) {
                    console.error(e);
                    util.MessageBox.Show(this, "添加失败:" + e.msg);
                }
            }
        },
    }
</script>

<style>

</style>
