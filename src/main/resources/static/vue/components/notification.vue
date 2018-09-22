<template>
    <div class="notif">
        <Badge dot  :count="count">
            <a href="#" class="demo-badge">
                <Icon type="ios-notifications-outline" size="26"></Icon>
            </a>
        </Badge>

        <div class="panel">
            <Scroll :on-reach-bottom="handleReachBottom">
                <h3>待处理的通知</h3>
                <Card dis-hover v-for="(e, i) in list" :key="i" style="margin: 32px 0" :title="e.user_name">
                    {{ e.uid === -1 ? '用户':'管理员' }}
                </Card>
            </Scroll>
        </div>
    </div>
</template>

<script>
    import init from '../../js/init.js';

    const Bean = {
        id: 0,
        uid: 1,
        aid: 1,
        gid: 1,
        user_name: "Miyuki",
        description: "提交了开票申请",
        tm: 123,
        processed: 0
    };

    export default {
        data: () => ({
            count: 1,
            list: [
                Bean,Bean,Bean,Bean
            ],
        }),

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
        right: 0px;
        top: 0px;
        height: 100%;
        width: 250px;
    }

    .inside {
        width: 100%;
        overflow-y: auto;
    }
</style>