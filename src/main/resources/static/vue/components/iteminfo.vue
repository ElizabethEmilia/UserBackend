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
                        {{  }}
                    </h2>
                    <div >
                        适用于各种行业的个人独资企业的一站式服务产品，全程自动化操作，享政府核准超低税率。​
                    </div>
                    <Divider />

                    费用价格:
                    <span class="price">12000</span>元/年
                    <div>
                        <div class="button">
                            立即购买
                        </div>
                    </div>


                </Col>
            </Row>
        </div>

        <div v-else>
            正在加载商品
        </div>
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

    export default {
        name: "ItemInfo",

        data: () => ({
            iteminfo: init.tItem,
        }),
        computed: {
            bigImageStyle() {
                if (util.String.isNullOrEmpty(this.iteminfo.image)) {
                    return {};
                }
                return {
                    backgroundImage: "url(" + image + ")"
                };
            },

        }
    }
</script>

<style scoped>

    .bigimage {
        height: 300px;
        background-color: #eee;
        margin-right: 15px;
        background-size: 100% auto;
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
        font-size: 15px;
        font-weight: bold;
    }
</style>