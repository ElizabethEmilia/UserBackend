<template>
    <div>
        <Overview @on-edit-mode-change="onOverviewEditModeChange" />
    </div>
</template>

<script>
    import ModifyPassword from './admin/adminmodifypwd.vue';
    import Overview from './admin/adminoverview.vue';

    export default {
        components: {
            ModifyPassword, Overview
        },
        data: () => ({
            overviewEditMode: false,
        }),
        methods: {
            onOverviewEditModeChange(val) { this.overviewEditMode = val; }
        }
    }
</script>

<style>

</style>