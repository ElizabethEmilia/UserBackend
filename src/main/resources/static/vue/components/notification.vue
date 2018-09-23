<template>
    <div class="notif">
        <Badge number  :count="count">
            <a href="#" class="demo-badge" @click="panelVisible = true">
                <Icon type="ios-notifications-outline" size="26"></Icon>
            </a>
        </Badge>

        <div class="panel" v-if="panelVisible">
            <div style="height: 100%">
                <div style="height: 60px;">
                    <h3 style="padding: 10px; float: left; padding-top: 15px;" >待处理的通知</h3>
                        <span class="close-btn" @click="panelVisible = false">&times;</span>
                </div>
                <div class="it" v-for="(e, i) in list" :key="i">
                    <p style="font-size: 12px; color: gray">{{ toDateString(e.tm) }}</p>
                    {{ e.uid !== -1 ? '用户':'管理员' }} {{ e.user_name }} {{ e.description }}
                    <p>
                        <a href="javascript:void(0)" @click="processNotification(i, e.id)">标记为“已处理”</a>
                    </p>
                </div>

                <div class="it" style="text-align: center">
                    <a href="javascript:void(0)" @click="updateMessage" :disabled="loading">{{ !loading ? '加载更多':'正在加载' }}</a>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import init from '../../js/init.js';
    import util from '../../js/util.js';
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
            list: [

            ],
            loading: false,
            currentPage: 1,

            panelVisible: false,
        }),
        methods: {
            async processNotification(i, id) {
                //this.loading = true;
                try {
                    await API.Notification.process(id);
                    //this.list[i].processed = 1;
                    this.list.splice(i, 1);
                    //this.loading = false;
                }
                catch (e) {
                    console.error(e);
                    //this.loading = false;
                }
            },
            async updateMessage() {
                this.loading = true;
                try {
                    let r = await API.Notification.getList(this.currentPage++);
                    this.list = this.list.concat(r);
                    this.loading = false;
                }
                catch (e) {
                    console.error(e);
                    this.loading = false;
                }

            },
            toDateString(ts) {
                return util.Date.toTimeStringFromTimestamp(ts);
            }
        },
        computed: {
            count() { return this.list.length; }
        },
         mounted() {
            this.updateMessage();
        }
    }
</script>

<style scoped>
.notif {
    position: fixed;
    bottom: 20px;
    right: 20px;
    z-index: 99999;
}

.demo-badge{
    width: 42px;
    height: 42px;
    background: #eee;
    border-radius: 6px;
    display: inline-block;
    padding: 8px;
}

    .panel {
        position: fixed;
        background: #fff;
        right: 0px;
        top: 0px;
        height: 100%;
        width: 250px;
        border-left: 1px solid #eee;
        z-index: 99999;
        overflow-y: auto;
        box-shadow: 3px 4px 5px 3px #aaa;
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

    .close-btn {
        float: right;
        font-size: 26px;
        display: inline-block;
        height: 30px;
        width: 30px;
        text-align: center;
        line-height: 30px;
        margin: 10px;
        border: 1px solid transparent;
    }

    .close-btn:hover {
        border: 1px solid blue;
        background: lightblue;
    }

    .close-btn:active {
        border: 1px solid purple;
        background: darkblue;
        color: white;
    }
</style>