<template>
    <Card  class="card-margin">
         <Divider orientation="left"><h3>系统配置</h3></Divider>
       
            <Alert type="success">
                提示：在此处可以配置系统中大部分可以配置的项。修改设置后，设置自动保存
            </Alert>

            <div v-if="fetchState == fetchStates.failed" style="margin-top: 15px; margin-left: 30px; color: red"  >
                无法获取系统配置项，请<a href="javascript:void(0);">重试</a>。
            </div>

            <div style="margin-top: 15px;"  v-for="(e,i) in settings" :key="i" >
                <ConfigItem :name="e.name" :value="''+e.value" :friendly-name="e.friendlyName" :type="e.typeString" :disabled="e.disabled" :desc="e.description" />
            </div>
    </Card>
</template>

<script>

import ConfigItem from './configitem.vue';
import { settingTypes } from '../../../constant.js';

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';

// 获取数据之前需先处理

export default {
    components: {
        ConfigItem
    },
    data: () => ({
        settingTypes,
        settings: [
            
        ],
        fetchStates: { pending: 1, success: 0, failed: 2 },
        fetchState: 1,
    }),
    methods: {
        async getList() {
            try {
                let result = await $.ajax('/api/system/settings');
                if (result.code == 0) {
                    // 处理数据以兼容后端
                    for (let i=0; i<result.data.length; i++) {
                        if (!result.data[i].friendlyName)
                            result.data[i].friendlyName = result.data[i].friendlyname;
                        if (!result.data[i].typeString)
                            result.data[i].typeString = settingTypes[result.data[i].type];
                    }
                    
                    this.settings = result.data;
                    
                    // 更新到localstorage缓存
                    if (window.localStorage) {
                        window.localStorage.setItem("_Miyuki_settings", JSON.stringify(result.data));
                    }

                    this.fetchState = this.fetchStates.success;
                }
                else {
                    this.fetchState = this.fetchStates.failed;
                }
            }
            catch(err) {
                this.fetchState = this.fetchStates.failed;
            }
        }
    },
    created() {
        // 从localstorage缓存读取设置项，以保证可以立即显示
        if (window.localStorage) {
            try {
                let s = window.localStorage.getItem("_Miyuki_settings");
                if (s != null && !s)
                    this.settings = JSON.parse(s);
            }
            catch(err) {
                copnsole.log(err);
            }
        }

        this.getList();
    }
}
</script>
