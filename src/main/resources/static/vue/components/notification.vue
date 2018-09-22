<template>
    <div class="notif">
        <Badge dot  :count="count">
            <a href="#" class="demo-badge">
                <Icon type="ios-notifications-outline" size="26"></Icon>
            </a>
        </Badge>

        <div class="panel">

        </div>
    </div>
</template>

<script>
    export default {
        data: () => ({
            count: 1,
        })
    }
</script>

<style scoped>
.notif {
    position: fixed;
    bottom: 20px;
    right: 20px;
}

.demo-badge{
    width: 42px;
    height: 42px;
    background: #eee;
    border-radius: 6px;
    display: inline-block;
}

    .panel {
        position: fixed;
        background: #fff;
        background: rgba(255,255,255,0.8);

    }
</style>