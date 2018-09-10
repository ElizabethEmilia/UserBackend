<template>
    <!-- 修改密码 -->
    <Card title="修改密码"  :bordered="false" dis-hover>
        <div style="margin-top: 5px; margin-left: 0px;">
            <div class="line-margin">
                <span class="title-before-input">原密码</span>
                <Input type="password" style="width: 200px;" v-model="old" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input">新密码</span>
                <Input type="password" style="width: 200px;" v-model="New" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input">再次输入</span>
                <Input type="password" style="width: 200px;" v-model="re" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input"></span>
                <Button :loading="pending" type="primary" @click="modify">修改密码</Button>
            </div>
        </div>
    </Card>
</template>

<script>

    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';
    import util from '../../../js/util.js';
    import $ from '../../../js/ajax.js';
    import md5 from 'js-md5';

    export default {
        data: () => ({
            old: '',
            New: '',
            re: '',

            doesNotMatch: true,
            pending: false,
        }),
        methods: {
            // 修改密码
            async modify() {
                if (this.pending)
                    return;
                if (util.String.isNullOrEmpty(this.old))
                    return util.MessageBox.Show(this, '请输入原密码');
                if (!util.String.isVividPassword(this.New))
                    return util.MessageBox.Show(this, '新密码格式不符合要求');
                if (this.New != this.re)
                    return util.MessageBox.Show(this, '两次输入的密码不相同');
                this.pending = true;
                try {
                    let result = await $.ajax('/api/account/password', {
                        old_pwd: md5(this.old),
                        new_pwd: md5(this.New),
                    });
                    this.pending = false;
                    if (result.code) {
                        return util.MessageBox.Error(this, result.msg);
                    }
                    util.MessageBox.Error(this, '修改密码成功');
                }
                catch(err) {
                    this.pending = false;
                    util.MessageBox.Error(this, '修改密码失败');
                }
            },
        }
    }
</script>