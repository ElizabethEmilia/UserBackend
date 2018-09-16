<template>

    <div class="center">
        <div class="outbox">
            <h2>确认购买信息</h2>

            <div class="item">
                <div class="img" :style="bigImageStyle"></div>
                <div class="name">{{ productName }}</div>
            </div>

            <h2>付款方式</h2>

            <img @click="onlinePayMethod=0" :class="['payment',onlinePayMethod==0?'selected':'']" :src="'../../../images/wechat_pay.png'" />
            <img @click="onlinePayMethod=1" :class="['payment',onlinePayMethod==1?'selected':'']" :src="'../../../images/alipay.png'" />
            <p style="color: gray" v-if="isNotCharge">
                若账户中的余额足够完成此次交易，将优先扣除账户中的余额。
            </p>

        </div>

        <div class="start">
            <div class="paybox">
                付款金额：<span class="price">{{ totalAmount<=0 ? '--' : totalAmount }}</span>元

                <br/><br/>

                <div v-if="!loading" class="button"  @click="startPay">前往支付</div>
                <div v-else class="disabled-button">前往支付</div>

                <p>
                    <a class="btn-gray" href="javascript:void(0)" @click="cancelPay">取消订单</a>
                </p>

            </div>
        </div>
    </div>
</template>

<script>
    import util from '../../../js/util.js';
    import API from '../../../js/api.js';

    let payment = ['alipay', 'wepay']

    export default {
        props: [ 'param' ],
        data: () => ({
            onlinePayMethod: -1,
            onlinePayAmount: 0,
            loading: true,

            productName: '--',
            image: '',
            totalAmount: 0,
            isNotCharge: true,
        }),
        methods: {
            async startPay() {
                if (this.onlinePayMethod == -1)
                    return util.MessageBox.Show(this, "请选择支付方式");
                // 原来的方法是window open现在不是了
                //let r = await API.Payment.Alipay.start(this.param);
                //util.Network.POSTInNewWindow()
                window.open('/api/pay/'+this.payMethod+'/start?'+ this.generateStartPrarms());
                this.$emit("on-start-pay", this.payMethod);
            },
            cancelPay() {
                this.$emit("on-cancel");
            },
            async loadItem(itemid) {
                try {
                    let r = await API.ShopItems.get(itemid);
                    this.productName = r.name;
                    this.totalAmount = r.price;
                    this.image = r.image;
                }
                catch (e) {
                    console.error(e);
                    await util.MessageBox.ShowAsync(this, "加载商品信息失败");
                    this.$emit("on-cancel");
                }
            },
            generateStartPrarms() {
               return util.forGetParams(this.param);
            },
            async init() {
                // 判断参数
                if (!util.isNullOrUndefined(this.param.itemid)) {
                    /// 购买商品表中的商品
                    this.loadItem(this.param.itemid);
                }
                else if (!util.isNullOrUndefined(this.param.amount)) {
                    if (!util.String.isNullOrEmpty(this.param.name)) {
                        /// 购买商品
                        this.productName = this.param.name;
                        this.totalAmount = this.param.amount;
                    }
                    else {
                        /// 充值
                        this.productName = "在线充值";
                        this.totalAmount = this.param.amount;
                        this.isNotCharge = false;
                    }
                }
                else {
                    console.error('invalid argument');
                    await util.MessageBox.ShowAsync(this, "INTERNAL_ERROR: Invalid arguement");
                    this.$emit("on-cancel");
                }
                this.loading = false;
            },
        },
        computed: {
            payMethod() {
                return ['alipay', 'wepay'][this.onlinePayMethod];
            },
            bigImageStyle() {
                if (util.String.isNullOrEmpty(this.image)) {
                    return {};
                }
                return {
                    backgroundImage: "url(" + this.image + ")"
                };
            },
        },
        watch: {
            param: {
                deep: true,
                handler() {
                    this.init();
                }
            }
        }

    }
</script>

<style scoped lang="less">
    @import "../../../css/initstyle.less";

    div.center {
        max-width: 1200px; min-width: 800px; margin: 0 auto;
    }

    .payment {
        opacity: 0.6;
        cursor: pointer;
        border: 2px solid #eee;
        padding: 10px;
        margin: 10px;
        margin-right: 5px;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        border-radius: 4px;
    }

    .selected {
        opacity: 1;
        border: 2px solid orangered;
    }

    div.item {
        background: #efefef;
        height: 100px;
        width: 100%;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        border-radius: 5px;
        position: relative;
        margin-top: 40px;
        margin-bottom: 30px;
    }

    div.item div.img {
        height: 100px;
        width: 100px;
        background: #f8f8f8;
        position: absolute;
        left: 0px;
        background-repeat: no-repeat;
        background-size: auto 100%;
    }

    div.item div.name {
        position: absolute;
        left: 100px;
        height: 100px;
        width: 700px;
        padding: 20px;
        font-size: 18px;
    }

    div.start {
        margin-top: 40px;
        position: relative;
        height: 140px;
    }

    div.paybox {
        position:absolute;
        right: 10px;
        text-align: right;
    }

    span.price {
        color: orangered;
        font-size: 30px;
        font-weight: bold;
    }

    div.disabled-button {
        background: #888;
        padding: 5px;
        display: inline-block;
        width: 200px;
        height: 40px;
        font-size: 20px;
        color: #ccc;
        line-height: 30px;
        text-align: center;
    }

    div.button {
        background: rgba(255,87,0,1);
        padding: 5px;
        display: inline-block;
        width: 200px;
        height: 40px;
        font-size: 20px;
        color: #fff;
        line-height: 30px;
        text-align: center;
        cursor: pointer;
    }

    div.button:hover {
        background: rgba(255,120,0,1);
    }

    div.button:active {
        background: rgba(200,50,0,1);
    }

    div.btn-gray {
        background: #eee;
        padding: 5px;
        display: inline-block;
        width: 200px;
        height: 40px;
        font-size: 20px;
        color: #555;
        line-height: 30px;
        text-align: center;
        cursor: pointer;
    }

    div.btn-gray:hover {
        background: #f3f3f3;
    }

    div.btn-gray:active {
        background: #ddd;
    }

    a.btn-gray {
        color: gray;
        font-size: 13.5px;
    }
    
    div.outbox1 {
        border: 2px solid #ddd;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        border-radius: 5px;
        padding: 15px;
    }

</style>