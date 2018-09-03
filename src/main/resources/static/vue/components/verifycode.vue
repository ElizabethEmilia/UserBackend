<template>
    <div style=" margin-top: 20px;">
        <Poptip style="">
            <Input :prefix="prefix" placeholder="图片验证码" style="width: 300px;" v-model="code" />
            <div slot="content">
                <div class="codeimg" :style="{ 'background': img === null ?  '#aaa' : img }" title="点击刷新" @click="refresh"/>
            </div>
        </Poptip>
    </div>
</template>

<script>

/**
 * 事件：
 *   on-success: 获取图片成功
 *   on-error:   获取图片失败
 *   on-refresh: 刷新图片触发
 *   on-code:    输入验证码触发
 */

import $ from '../../js/ajax.js';
import util from '../../js/util.js';

export default {
    data: () => ({
        img: null,
        code: '',
    }),
    props: [ 'prefix' ],
    methods: {
        async getImageAsync() {
            try {
                let result = await $.ajax('/api/verifycode');
                if (result.code === 0) {
                    this.img = result.data;
                    this.$emit('on-success', result);
                }
                else {
                    throw new Error(result);
                }
            }
            catch (err) {
                console.error('[Verify code]', 'failed to get verify code', err);
                this.$emit('on-error', err);
            }
        },
        refresh() {
            this.$emit('on-refresh', {});
            this.getImage();
        },
        getImage() {
            this.img = 'url(/api/verifycode?r='+Math.random()+')';
        },
    },
    watch: {
        code(val) {
            this.$emit('on-code', val);
        }
    },
    mounted() {
        this.getImage();
    }
}
</script>

<style>
div.codeimg {
    width: 200px;
    height: 100px;
    cursor: pointer;
}

div.codeimg:active {
    opacity: 0.9;
}
</style>
