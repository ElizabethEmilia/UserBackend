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
                <ConfigItem :name="e.name" :value="''+e.value" :friendly-name="e.friendlyName" :type="e.typeString" :disabled="e.disabled" />
            </div>
    </Card>
</template>

<script>

/// TODO: 将系统配置项加载进缓存。

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
                    this.settings = result.data;
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
        this.getList();
    }
}
</script>
