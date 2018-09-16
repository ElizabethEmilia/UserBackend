<template>
    <div>
        <div class="banner">
            产品介绍
        </div>

        <div class="center" style="font-size: 13.5px; width: 1000px; ">
            <ItemInfo />
        </div>
    </div>
</template>

<script>
    import ItemInfo from '../iteminfo.vue';

    export default {
        components: {
            ItemInfo,
        },
        data: () => ({

        }),
        mounted() {
            document.documentElement.style.background = '#fff';
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

    div.center {
        max-width: 1200px; min-width: 600px; margin: 0 auto;
    }
</style>