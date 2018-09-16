<template>
    <div style="background: #fff"  >
        <!--
        要求付款界面，需要后端注入是否付费的字段

        如果不付费则不加载mainframe  直接加载本界面

        另外，如果用户在界面内部选择购买续期，也同样转到该界面

        该界面的数据内容从商品表获取
        -->

        <div class="banner-top">
            <div style="max-width: 1200px; min-width: 600px; margin: 0 auto;  position: relative;">
                <div class="inner-right">
                    <a href="/logout">退出登录</a>
                </div>
            </div>

        </div>

        <ProductIntroduce />

    </div>
</template>

<script>
    import ItemInfo from '../iteminfo.vue';
    import ProductIntroduce from './productintroduce.vue';

    export default {
        components: {
            ItemInfo, ProductIntroduce,
        },
        data: () => ({

        }),
        methods: {

        },
        watch: {

        }
    }
</script>

<style scoped>

    .banner-top {
        width: 100%;
        height: 60px;
        line-height: 60px;
        font-size: 13.5px;
        background: #222;
        color: white;
    }

    .banner-top .inner-left {
        position: absolute;
        left: 0px;
        height: 60px;
        font-size: 20px;
    }

    .banner-top .inner-right {
        position: absolute;
        right: 0px;
        height: 60px;
    }

    .banner-top a {
        color: #eee;
        text-decoration: none;
    }

.banner {
    width: 100%;
    height: 170px;
    line-height: 170px;
    font-size: 35px;
    text-align: center;
    background: #333;
    color: white;
}
</style>