<template>
    <div class="updmain" @click="selectFile">
        <div >
            
            <input ref="openFile" @change="getFileContent" type="file" style="display:none" />
            <div style="padding: 60px 0" v-if="base64 == null">
                <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                <p>{{ title }}</p>
                <p style="font-size: 12px; color: #999;">仅支持5MB以内的图片</p>
            </div>
            
            <div v-else style="position:relative">
                
                <p style="margin-top: 40px"></p>
            </div>
            <div v-if="base64" :style="styles" class="prv blur"> </div>
            <div v-if="base64" class="prv blur-mask"> </div>
            <div v-if="base64" :style="styles" class="prv disp">
                <div style="position: relative">
                    <div style="font-size: 15px; font-weight: blod;width: 100%; bottom: -233px; position: absolute; text-align: center">
                        {{ fileName }} <span style="color: gray">({{ fileSize }})</span>
                    </div>
                </div>
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
    props: [ 'title', 'value' ],
    data: () => ({
        fileName: '',
        fileSize: '',
        base64: null,
    }),
    computed: {
        styles() {
            return Object.assign(
                this.base64 ? { backgroundImage: 'url(' + this.base64 + ')' }:{}
            );
        },
    },
    methods: {
        selectFile() {
            this.$refs.openFile.click();
        },
        // 获取文件内容
        async getFileContent(e) {
            try {
                let fileContent = await util.File.getFileContentAsync(this.$refs.openFile);
                this.fileName = fileContent.name.name;
                this.fileSize = util.File.friendlySize(fileContent.size);
                console.log('[FileUpload] 上传的文件名：', fileContent.name.name);
                console.log('[FileUpload] 上传的文件大小：' + util.File.friendlySize(fileContent.size));
                this.base64 = fileContent.data;
                this.$emit('input', fileContent);
            }
            catch(err) {
                console.error(err);
            }
        },
    },
    watch: {
        //value(v) { this.base64 = v; },
    }
}
</script>

<style>
div.updmain {
    margin: 10px;
    margin-left: 25px;
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
    
        background-size: 100% auto;
    position: absolute;
    top: 0px;
    left: 20px;
    height: 100%;
    /* width: 100%; */
    right: 10px;
    border-radius: 6px;
}

.disp {
    bottom: 50px;
    height: initial;
}

.blur {
    filter: blur(5px);
}

.blur-mask {
    background-color: rgba(255,255,255,0.8)
}
</style>
