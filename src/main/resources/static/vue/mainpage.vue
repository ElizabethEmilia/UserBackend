<template>
    <Row>
        <Col span="4">
            <Navigator @menuselect="menusel"/>
        </Col>
        <Col span="12">
            <router-view></router-view>
        </Col>
    </Row>
</template>

<script>
    import Navigator from './navigator.vue';

    export default {
        components: {
            Navigator
        },
        data: () => ({
            
        }),
        methods: {
            menusel(name) {
                this.$router.push('/'+name);
                //alert('Selected: ' + name);
            }
        }
    }
</script>