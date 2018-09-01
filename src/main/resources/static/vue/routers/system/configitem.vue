<template>
    <div class="item">
        <div class="inline title">
            {{  }}
        </div>
        <div class="inline input"></div>
    </div>
</template>

<script>

/**
 * 配置项
 * 
 * 事件：
 *   on-load:   配置项读取成功
 *   on-invalid-type:  未知的数据类型
 *   on-error:  更新数据项失败
 * 
 * 参数：
 *   friendlyName  设置名
 *   name          同上
 *   value         设置值
 *   type          设置类型
 */

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';

export default {
    props: [
        'friendlyName', 'name', 'value', 'type'
    ],
    data: ()=>({

    }),
    methods: {

    },
    computed: {
        title() {
            if (util.isStringNullOrEmpty(this.friendlyName)) {
                return this.name;
            }
            return this.friendlyName;
        },
        subtitle() {
            if (util.isStringNullOrEmpty(this.friendlyName)) {
                return '';
            }
            return '(' + this.name + ')';
        }
    }
}
</script>

<style>
div.item {
    width: 500px;
    margin-left: 40px;
}

div.inline {
    display: inline-block;
}

div.title {
    width: 190px;
    margin-right: 10px;
}

div.subtitle {
    font-size: 12px;
    color: gray;
}

div.input {
    width: 300px;
}

div.
</style>
