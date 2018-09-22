<template>
    <div class="notif">
        <Badge dot  :count="count">
            <a href="#" class="demo-badge">
                <Icon type="ios-notifications-outline" size="26"></Icon>
            </a>
        </Badge>

        <div class="panel">
            <div style="height: 100%">
                <h3 style="padding: 10px;">待处理的通知</h3>
                <div class="it" v-for="(e, i) in list" :key="i">
                    {{ e.uid !== -1 ? '用户':'管理员' }} {{ e.user_name }} {{ e.description }}
                    <p>
                        <a href="javascript:void(0)" @click="processNotification(i, e.id)">标记为“已处理”</a>
                    </p>
                </div>

                <div class="it" style="text-align: center">
                    <a href="javascript:void(0)" @click="updateMessage">加载更多</a>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import init from '../../js/init.js';
    import API from '../../js/api.js';

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

            currentPage: 1,
        }),
        methods: {
            async processNotification(i, id) {
                try {
                    await API.Notification.process(id);
                    //this.list[i].processed = 1;
                    this.list.splice(i, 1);
                }
                catch (e) {
                    console.error(e)
                }
            },
            async updateMessage() {
                let r = await API.Notification.getList(this.currentPage++);
                this.list = this.list.concat(r);
            }
        },
         mounted() {this.updateMessage();
        }
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
        right: 0px;
        top: 0px;
        height: 100%;
        width: 250px;
        border-left: 1px solid #888;
    }

    .inside {
        width: 100%;
        overflow-y: auto;
    }

    .it {
        padding: 10px;

    }

    .it:hover {
        background: #eee;
    }
</style>