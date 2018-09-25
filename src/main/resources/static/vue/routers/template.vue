<template>
    <Card class="card-margin" :title="title">
        <div style="margin: 10px;">
            选择要更改的模板：
            <Select v-model="templateIndex" style="width: 200px;">
                <Option v-for="(e, i) in content" :key="i" :value="i">{{ e.name }}</Option>
            </Select>
        </div>

        <div v-if="templateIndex >= 0">
            <VueEditor name="html-editor" style="height: 400px;" :height="400" v-model="editHTML"></VueEditor>

            <div style="margin: 60px 10px 10px 10px">
                <Button @click="save" type="success">保存更改</Button>
            </div>

            <div v-if="selectedTemplate.vars.length > 0">
                <h3>变量说明</h3>
                <p>变量嵌入到模板文本中，在实际显示时，变量会被替换为实际的文本，从而显示动态的内容。</p>
                <p>
                    可用的变量及其含义如下所示。<br/>
                    <span v-for="e in selectedTemplate.vars">
                        <a @click="append(e.name)" href="javascript:void(0)"><code>{{'{' + '{' + e.name + '}' + '}' }}</code></a> - {{ e.stands }} <br/>
                    </span>
                </p>
            </div>
        </div>

    </Card>
</template>

<script>
    import '../../css/style.less';
    import { VueEditor } from "vue2-editor";
    import util from '../../js/util.js';
    import API from '../../js/api.js';

    export default {
        name: "Template",
        components: {
            VueEditor,
        },
        data: () => ({
            title: '编辑模板',

            templateIndex: -1,
            editHTML: '',
            initHTML: '',

            content: [
                {
                    name: "合同模板",
                    settingName: "template_license",
                    vars: [
                        { name: "orderID", stands: "订单编号" },
                        { name: "companyName", stands: "公司名称" },
                        { name: "companyID", stands: "公司ID(系统内部表示)" },
                        { name: "customerName", stands: "客户名称" },
                        { name: "customerID", stands: "客户ID(系统内部表示)" },
                        { name: "amount", stands: "订单价格(精确到小数点后两位)" },
                        { name: "year", stands: "订单日期（年份）" },
                        { name: "month", stands: "订单日期（月份）" },
                        { name: "day", stands: "订单日期（日）" },
                        { name: "date", stands: "订单日期(yyyy年MM月dd日)" },
                        { name: "name", stands: "订单名称" },
                    ]
                },
                {
                    name: "服务条款模板",
                    settingName: "template_terms",
                    vars: [  ],
                }
            ]
        }),

        methods: {
            async loadTemplate(name) {
                try {
                    this.editHTML = this.initHTML = await API.Setting.get(name);
                }
                catch(e) {
                    console.error(e);
                    util.MessageBox.Show(this, '无法获取模板内容，请重试');
                }
            },
            async saveTemplate(name) {
                try {
                    await API.Setting.set(name, this.editHTML);
                    this.initHTML = this.editHTML;
                    this.$Notice.open({
                        title: '已保存',
                        desc: '对模板【' + this.selectedTemplate.name + "】的修改已保存。"
                    });
                }
                catch(e) {
                    console.error(e);
                    util.MessageBox.Show(this, '无法保存模板内容，请重试');
                }
            },
            append(what) {
                this.editHTML += `{{${what}}}`;
            },
            save() {
                this.saveTemplate(this.selectedTemplate.settingName);
            }
        },

        computed: {
            selectedTemplate() { return this.content[this.templateIndex]; }
        },

        watch: {
            templateIndex(val) {
                if (val === -1)
                    return;
                this.loadTemplate(this.selectedTemplate.settingName);
                if (this.initHTML !== this.editHTML) {
                   this.save();
                }
            }
        }

    }
</script>

<style scoped>

</style>