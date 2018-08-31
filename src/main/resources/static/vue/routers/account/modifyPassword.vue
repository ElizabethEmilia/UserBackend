<template>
    <!-- 修改密码 -->
    <Card class="card-margin">
        <Divider orientation="left"><h3>修改密码</h3></Divider>
        <div style="margin-top: 20px; margin-left: 30px;">
            <div class="line-margin">
                <span class="title-before-input">原密码</span>
                <Input type="password" style="width: 200px;" v-model="old" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input">新密码</span>
                <Input type="password" style="width: 200px;" v-model="_new" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input">再次输入</span>
                <Input type="password" style="width: 200px;" v-model="re" /> 
            </div>
            <div class="line-margin">
                <span class="title-before-input"></span>
                <Button type="primary" @click="modify">修改密码</Button>
            </div>
        </div>
    </Card>
</template>

<script>

    import { industry, memberType, paymentMethod, publicOrderStatus } from '../../../constant.js';
    import '../../../css/style.less';

    export default {
        data: () => ({
            old: '',
            _new: '',
            re: '',
        }),
        methods: {
            modify() {
                alert('///TODO: modify password');
            }
        }
    }
</script>