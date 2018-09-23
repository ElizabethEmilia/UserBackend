<template>
    <!-- 大的商品详情 -->

    <div style="font-size: 14.5px; margin-top: 20px; width: 1000px; ">
        <div v-if="iteminfo != null">
            <Row>
                <Col span="8">
                    <div class="bigimage" :style="bigImageStyle">

                    </div>
                </Col>
                <Col span="16">
                    <h2>
                        {{ iteminfo.name }}
                    </h2>
                    <div >
                       {{ iteminfo.description }}
                    </div>
                    <Divider />

                    费用价格:
                    <span class="price">{{ iteminfo.price }}</span>元/年
                    <div>
                        <div class="button" @click="buyNow">
                            立即购买
                        </div>
                    </div>


                </Col>
            </Row>
        </div>

        <div v-else>
            正在加载商品
        </div>

        <Modal v-model="confirmDialogVisible" fullscreen >
            <div slot="footer">
            </div>

            <ConfirmOrders
                    @on-start-pay="val => {confirmDialogVisible = false; confirmResultDialog=true; payMethod = val;}"
                    @on-cancel="confirmDialogVisible = false;"
                    :param="{ amount: iteminfo ? iteminfo.price : 0 }"
                    :dst="0" />

        </Modal>

        <Modal
                :closable="false"
                :mask-closable="false"
                v-model="confirmResultDialog">
            <ConfirmResult
                :method="payMethod"
                @on-cancel="val => confirmResultDialog = false"
            />

            <div slot="footer">
            </div>

        </Modal>
    </div>
</template>

<script>
    /*
    * 参数：itemid
    *
    * 事件：on-buy(itemid) 发起购买
    * */

    import init from '../../js/init.js';
    import util from '../../js/util.js';
    import API from '../../js/api.js';
    import ConfirmOrders from '../routers/orders/confirmorders.vue';
    import ConfirmResult from './confirmchargeresult.vue';

    export default {
        name: "ItemInfo",
        components: {
            ConfirmOrders, ConfirmResult,
        },
        props: [
            'itemid'
        ],
        data: () => ({
            iteminfo: null,
            confirmDialogVisible: false,
            confirmResultDialog: false,
            payMethod: '',
        }),
        computed: {
            bigImageStyle() {
                if (util.String.isNullOrEmpty(this.iteminfo.image)) {
                    return {};
                }
                return {
                    background: "url(" + this.iteminfo.image + ")"
                };
            },

        },
        methods: {
            async getItem(id) {
                try {
                    this.iteminfo = await API.ShopItems.get(id);
                }
                catch (e) {
                    util.MessageBox.Show(this, "无法获取商品信息。(id=" + id +")");
                }
            },
            buyNow() {
                this.confirmDialogVisible = true;
                this.$emit("on-buy", this.itemid);
            }
        },
        mounted() {
            if (util.String.isNullOrEmpty(this.itemid)) {
                this.getItem(this.itemid);
            }
        }
    }
</script>

<style scoped>

    .bigimage {
        height: 300px;
        background: #eee;
        margin-right: 15px;
        background-size: 100% auto;
        background-repeat: no-repeat;
    }

    div.button {
        background: rgba(255,87,0,1);
        padding: 5px;
        display: inline-block;
        width: 200px;
        height: 50px;
        font-size: 20px;
        color: #eee;
        line-height: 40px;
        text-align: center;
        cursor: pointer;
    }

    div.button:hover {
        background: rgba(255,120,0,1);
    }

    div.button:active {
        background: rgba(200,50,0,1);
    }

    span.price {
        color: orangered;
        font-size: 28px;
        font-weight: bold;
    }
</style>