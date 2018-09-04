<template>
    <div class="updmain" :style="style" @click="selectFile">
        <div >
            
            <input ref="openFile" @change="getFileContent" type="file" style="display:none" />
            <div style="padding: 60px 0" v-if="base64 == null">
                <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                <p>{{ title }}</p>
                <p style="font-size: 12px; color: #999;">仅支持5MB以内的图片</p>
            </div>
            
            <div v-else style="position:relative">
                
                <p style="margin-top: 40px">filename.png</p>
            </div>
            <div :style="styles" class="prv">
                    
            </div>
        </div>
    </div>
</template>
<script>

/*
*   style: 样式
    title: 标题
    url: 上传地址
    v-model: 文件base64字符串

    事件：
    on-select-file(filename): 选择文件
    input(filename):          v-model
*/

import util from '../../js/util.js';

export default {
    props: [ 'style', 'title', 'value' ],
    data: () => ({
        
    }),
    computed: {
        styles() {
            return Object.assign(
                this.base64 ? { backgroundImage: 'url(' + this.base64 + ')' }:{}
            );
        },
        base64() {
            return this.value;
        }
    },
    methods: {
        selectFile() {
            this.$refs.openFile.click();
        },
        // 获取文件内容
        async getFileContent(e) {
            let fileContent = await util.File.getFileContentAsync(this.$refs.openFile);
            this.value = fileContent;
            this.$emit('input', fileContent);
        },
    },
}
</script>

<style>
div.updmain {
    margin: 10px;
    border: 1px #aaa dashed;
    border-radius: 3px; 
    height: 230px;
    text-align: center;
    cursor: pointer;
}

div.updmain:hover {
    border: 1px lightblue dashed;
}

.prv {
    background-size: 100% 100%;  position: absolute; top: 0px; left: 0px; height: 100%; width: 100%;
}
</style>
