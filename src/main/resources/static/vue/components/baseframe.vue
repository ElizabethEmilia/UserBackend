<template>
    <Card class="parent-card" dis-hover :bordered="false" :style="{height: frameHeight + 'px'}">
        
    </Card>
</template>

<script>

/* 
    界面元素的父组件

    事件:
        on-request-change-com (name)  - 请求转换组件时触发

*/
    
export default {
    data: () => {
        frameHeight: 400
    },
    methods: {
        show(name) {
            this.$emit('on-request-change-com', name);
        },
    }
}
</script>

<style>
.parent-card {
    text-align: center; background-color: #eee; background-color:rgba(255, 255, 255, 0.4); width: 450px; 
}
</style>
