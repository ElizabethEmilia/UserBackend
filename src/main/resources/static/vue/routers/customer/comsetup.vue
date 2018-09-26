<template>
    <Card  class="card-margin">
        <Divider orientation="left"><h3>公司设立进度</h3></Divider>

        <PagedTable ref="dt" :columns="columns" :data-source="`customer/_/company/${cid}/setup`" />
        <div style="margin-top: 20px;">
            <Button type="success" @click="dialogVisible = true">新增设立进度</Button>
        </div>
        <div style="margin-top: 20px;">

        </div>

        <Modal
            :width="600"
            title="新增设立进度"
            v-model="dialogVisible"
            @on-ok="add"
        >
            <div style="margin: 15px 0 15px 0">

                <div class="dm">
                    <span class="title-before-input"> <i class="required" />状态 </span>
                    <AutoComplete
                            v-model="newProgress.state"
                            placeholder=""
                            style="width:200px">
                        <div class="demo-auto-complete-item" >
                            <Option class="hov" v-for="(e, i) in states" :value="e" :key="i">
                                <span class="demo-auto-complete-title">{{ e }}</span>
                                <span class="demo-auto-complete-count" @click="states.splice(i,1)">
                                    删除
                                </span>
                            </Option>
                        </div>
                        <a href="JavaScript:void(0)" class="demo-auto-complete-more" :disabled="isNewState" @click="states.push(newProgress.state)">保存为新状态</a>
                    </AutoComplete>

                    <div class="dm">
                        <span class="title-before-input"> <i class="required" />说明</span>
                        <Input v-model="newProgress.note" style="width: 200px"  />
                    </div>
                </div>
            </div>
        </Modal>
    </Card>
</template>

<script>
    import $ from '../../../js/ajax.js';
    import util from '../../../js/util.js';
    import PagedTable from '../../pagedTable.vue';
    import md5 from 'js-md5';
    import API from '../../../js/api.js';
    import init from '../../../js/init.js';

    export default {
        props: [ 'cid' ],
        components: {
            PagedTable,
        },
        data: () => ({
            states:  window.config.setup_states.split(','),
            newProgress: init.tComSetProgress,
            dialogVisible: false,
            columns() {
                return [
                    { title: '序号', type: 'index' },
                    { title: '公司ID', key: 'name' },
                    { title: '公司名称', key: 'cid' },
                    { title: '时间', key: 'tm' }, //string
                    { title: '状态', key: 'status' }, /// string
                    { title: '备注', key: 'note' }, // string
                    {
                        title: '操作',
                        render: (h,p)=>h('a', {
                            on: {
                                click: async () => {
                                    await util.MessageBox.ComfirmAsync(this, "确认要删除吗？");
                                    await API.Company.SetupProgress.remove(p.row.cid, p.row.id);
                                }
                            }
                        }, '删除')
                    }
                ];
            }
        }),
        methods: {
            async add() {
                let I = this.newProgress;

                if (this.cid == undefined || this.cid == null ||this.cid <=-1) {
                    return util.MessageBox.Show(this, "请选择公司");
                }
                I.cid = this.cid;

                if (util.String.isNullOrEmpty(I.state)) {
                    return util.MessageBox.Show(this, "请输入公司状态");
                }

                try {
                    let r = await API.Company.SetupProgress.add(this.cid, {
                        status: I.state,
                        note: I.note,
                    });
                    util.MessageBox.Show(this, "添加成功");
                    this.$refs.dt.refresh();
                }
                catch (e) {
                    console.error(e);
                    util.MessageBox.Error(this, "添加失败, " + e.message);
                }
            }
        },
        watch: {
            states: {
                deep: true,
                async handler(val) {
                    try {
                        let t = await API.Company.SetupStates.update(val);
                        console.log(t)
                        window.config.settings.setup_states = this.states.join(',');
                        console.log("[UpdateStatus]", "更新状态表成功");
                    }
                    catch(err) {
                        console.log(err);
                        this.states = window.config.settings.setup_states.split(',');
                        util.MessageBox.Show(this, "更新状态表失败, " + err.message);
                    }
                }
            }
        },
        computed: {
            isNewState() {
                return this.states.length !== 0 && this.states.filter(e=>e===this.newProgress.state).length !== 0;
            }
        },
        mounted() {

        },
    }
</script>

<style>
    .demo-auto-complete-item{
        padding: 4px 0;
        border-bottom: 1px solid #F6F6F6;
    }
    .demo-auto-complete-group{
        font-size: 12px;
        padding: 4px 6px;
    }
    .demo-auto-complete-group span{
        color: #666;
        font-weight: bold;
    }
    .demo-auto-complete-group a{
        float: right;
    }
    .demo-auto-complete-count{
        float: right;
        color: #999;
        opacity: 0;
    }
    .demo-auto-complete-more{
        display: block;
        margin: 0 auto;
        padding: 4px;
        text-align: center;
        font-size: 12px;
    }
    .hov:hover>.demo-auto-complete-count {
        opacity: 1;
    }
</style>
