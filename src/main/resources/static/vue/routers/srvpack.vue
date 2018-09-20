<template>
    <Card title="将于到期或已到期的服务包列表">

        <div style="margin-bottom: 12px;">
            <p v-if="isAdmin">下表示即将到期的公司。</p>
            <p v-else>下表示即将到期的公司，请到【我的账户/在线充值】下充值足够的金额，我们将在到期日次日自动续期。</p>

        </div>

        <PagedTable ref="data" :columns="columns" data-source="company/deadline" />

        <Modal v-model="dialogShouldOpen" title="为客户公司续期">
            <p>
                续期月数 <br />
                <InputNumber v-model="params.months" style="width: 200px;"/>
            </p>
            <p>
                续期价格 <br />
                <InputNumber v-model="params.price" style="width: 200px;"/>
            </p>
            <div slot="footer">
                <Button type="success" @click="renew">续期</Button>
                <Button @click="dialogShouldOpen = false">取消</Button>
            </div>
        </Modal>
    </Card>
</template>

<script>
    import PagedTable from '../pagedTable.vue';
    import AdminInfo from './user/admininfo.vue';
    import API from '../../js/api.js';
    import util from '../../js/util.js';
    import render from '../../js/render.js';

    export default {
        components: {
            PagedTable,
        },
        data: function() {
            let _this = this;

            return {
                addMonth: 12,
                selectedIndex: -1,
                isAdmin: window.config.isAdmin,

                params: {
                    price: 12000,
                    months: 12,
                },
                columns() {
                    let self = this;

                    return [
                        { title: '序号', type: 'index', width: 100 },
                        { title: '用户名', key: 'userName' },
                        { title: '公司名称', key: 'name', },
                        { title: '服务包到期时间', key: 'oriTaxPackEnd' },
                        window.config.isAdmin ? {
                            title: '操作',
                            width: 150,
                            render: (h,p) => h('div', [
                                render.link(h, p, '为客户续期', function() {
                                    _this.selectedIndex = p.index;
                                })
                            ])
                        } : { title: '费用', render: h=>h('span', {}, '12000元') },
                    ]
                }
            }
        },
        computed: {
            selectedItem() {
                return this.$refs.data.d[this.selectedIndex];
            },
            inputMonths() {
                return this.params.months;
            },
            dialogShouldOpen: {
                get() { return this.selectedIndex !== -1; },
                set(val) { val || (this.selectedIndex = -1, this.params = {
                    price: 0,
                        months: 12,
                }); }
            }
        },
        methods: {
            async renew() {
                if (util.isNullOrUndefined(this.selectedItem))
                    return util.MessageBox.Show(this, "请选择一个用户");

                let months = Number(this.params.months);
                let price = Number(this.params.price);
                if (isNaN(price) || price < 0)
                    return util.MessageBox.Show(this, "价格请输入数字");
                if (isNaN(months) || months < 0)
                    return util.MessageBox.Show(this, "月份请输入数字");
                let addi = "";
                let stantard = this.params.months * 1000;
                if (price < stantard) {
                    addi = "(该价格低于标准价格)";
                    if (!window.config.P.SetDiscount)
                        return util.MessageBox.Show(this, "您没有权限为客户设置打折价。");
                }
                else if (price > stantard) {
                    addi = "(该价格高于标准价格)";
                    if (!window.config.P.SetDiscount)
                        return util.MessageBox.Show(this, "您没有权限为客户设置高于标准价格的价格。");
                }

                await util.MessageBox.ComfirmAsync(this, "确实要以"
                    +this.params.price+"元的价格，" + addi +
                    "给客户"+this.selectedItem.userName+"(uid:"+this.selectedItem.uid+")的公司[" +
                    this.selectedItem.name + "]" +
                    "续期"+this.params.months+"个月吗？");

                try {
                    await API.Customer.Company.renew(this.selectedItem.uid, this.selectedItem.id, this.params);
                    this.$refs.data.refresh();
                    alert("操作成功");
                    this.selectedIndex = -1;
                }
                catch (e) {
                    console.error(e);
                    alert("操作失败" + (e.msg ? '' : e.msg));
                    this.selectedIndex = -1;

                }
            }
        },
        watch: {
            inputMonths() {
                this.params.price = 1000 * this.params.months;
            },
        }
    }
</script>