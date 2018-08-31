<template>
    <!-- 发送短信验证码组件 -->
    <div>
                <div style="width: 300px; margin: 0 auto; margin-top: 20px;">
                    <Row>
                        <Col span="15">
                            <Input class="tp" v-model="inputPhone" :disabled="msgsent" :maxlength="11" placeholder="手机号码" style="" />
                        </Col>
                        <Col span="6">
                            <Button style="width: 105px; margin-left: 5px;" 
                                :disabled="sendMessageButtonTimeCountdown != 0" 
                                @click="sendMessageCode"
                                v-html="sendMessageButtonTimeCountdown == 0 ? sendMessageText : '' +  sendMessageButtonTimeCountdown + '秒后重发'">
                                Send Message
                            </Button>
                        </Col>
                    </Row>
                   
                </div>

                <div>
                    <Input class="tp" v-model="msgcode" autofocus maxlength="16" placeholder="短信验证码" style="width: 300px; margin-top: 20px;" />
                </div>
    </div>
</template>

<script>

/**
 * 发送短信验证码组件
 * 
 * 事件
 *    on-send-msg: 在短信发送的时候触发 （参数：电话号码）
 *    on-can-resent: 在可以重发短信时触发（剩余0秒）
 *    on-send-failed: 在短信发送失败时触发
 *    on-sent: 在短信发送成功时触发
 *    on-input-msg-code: 在输入验证码时候触发 (参数  验证码)
 *    on-invalid-number: 手机号不正确触发
 * 
 * 参数
 *    verify-code: 图片验证码
 */

import $ from '../../js/ajax.js';
import util from '../../js/util.js';

export default {
    data: () => ({
        msgsent: false,
        sendMessageText: '发送验证码',
        sendMessageButtonTimeCountdown: 0,
        inputPhone: '',
        phone: '',
        interval: null,
        msgcode: '',
    }),
    props: [ 'verifyCode' ],
    methods: {
        // 发送短信
        async sendMessageCode() {
            // 检查手机号码
            if (! /^1\d{10}$/.test(this.inputPhone)) {
                this.$emit('on-invalid-number', this.inputPhone);
                return;
            }

            console.log('[MessageSend]', `verifycode=${this.verifyCode}  phone=${this.phone}`);

            this.$emit('on-send-msg', this.phone);
            
            if (!this.msgsent)
                this.phone = this.inputPhone; // 防止用户（熊孩子）改动，保存发送短信的正确手机号码
            this.msgsent = true;
            this.startCountDown();

            try {
                let result = await $.ajax('/api/sendmsg', util.forPostParams({
                    phone: this.phone,
                    code: this.verifyCode
                }));

                if (result.code === 0) {
                    // 发送成功
                    this.$emit('on-sent', result);
                    console.log('[MessageSend]', 'MESSAGE SENT SUCCESS');
                }
                else {
                    // 发送失败
                    this.$emit('on-send-failed', result);
                    console.error('[MessageSend]', 'Oh, sent failed!! God knows waht happened', result);
                    this.stopCountDown();
                }
            }
            catch (err) {
                this.$emit('on-send-failed', err);
                console.error('[MessageSend]', 'Oh, sent failed!! God knows waht happened', err);
                this.stopCountDown();
            }
        },

        // 开始倒计时
        startCountDown() {
            this.sendMessageButtonTimeCountdown = 60;
            this.interval = setInterval(() => {
                if (this.sendMessageButtonTimeCountdown <= 0) {
                    clearInterval(this.interval);
                    return;
                }
                this.sendMessageButtonTimeCountdown--;
            }, 1000);
        },

        // 停止倒计时
        stopCountDown() {
            clearInterval(this.interval);
            this.sendMessageButtonTimeCountdown = 0;
        }
    },
    watch: {
        msgcode(val) {
            this.$emit('on-input-msg-code', val);
        }
    }
}
</script>
