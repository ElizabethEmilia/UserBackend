<template>
    <div>
        <div :style="avatar ? { backgroundImage: 'url(' + avatar + ')' }:{}"
             class="avatar float-left avatarpos">
        </div>
        <h3 style="color: white">{{ username }}</h3>
    </div>
</template>

<script>
    export default {
        name: "avatar",
        avatar: window.config.avatar,
        username: window.config.username,
    }
</script>

<style scoped>

</style>