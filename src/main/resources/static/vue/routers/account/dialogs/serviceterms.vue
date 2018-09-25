<template>
    <div>
        <div v-if="!failed" v-html="t"></div>
        <div v-else>获取服务条款失败，请<a href="javascript:void(0)" @click="getTerms">重试</a></div>
    </div>
</template>

<script>
import $ from '../../../../js/ajax.js';

export default {
    props: ['content'],
    data: () => ({
        t: '',
        failed: false,
    }),
    methods: {
        async getTerms() {
            this.failed = false;
            this.t = '正在加载服务条款...';
            try {
                this.t = await $.ajax('/terms', undefined, undefined, undefined, "text");
            }
            catch (e) {
                console.error(e);
                this.failed = true;
            }
        }
    },
    mounted() {
        this.getTerms();
    }
}
</script>
