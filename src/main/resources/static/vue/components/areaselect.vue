<template>
    <Cascader class="ft" v-model="selected" :data="data" :load-data="loadData"></Cascader>
</template>
<script>
    import API from '../../js/api.js';
    import util from '../../js/util.js';

    export default {
        props: [ 'value' ],
        data () {
            return {
                data: [],
                selected: [],
            }
        },
        methods: {
            async loadData (item, callback) {
                item.loading = true;
                if (item.value.type === 'province') {
                    item.children = await this.loadCity(item.v.province);
                    item.loading = false;
                    callback();
                }
                else if (item.value.type === 'city') {
                    item.children = await this.loadDistrict(item.v.province, item.v.city);
                    item.loading = false;
                    callback();
                }
            },
            async loadProvince() {
                let result = await API.Area.getProvince();
                return result.map(e => ({
                    v: {
                        type: 'province',
                        province: e,
                    },
                    value: e,
                    label: e,
                    children: [],
                    loading: false,
                }))
            },
            async loadCity(province) {
                let result = await API.Area.getCity(province);
                return result.map(e => ({
                    v: {
                        type: 'city',
                        province,
                        city: e,
                    },
                    value: e,
                    label: e,
                    children: [],
                    loading: false,
                }));
            },
            async loadDistrict(province, city) {
                let result = await API.Area.getDistruct(province, city);
                return result.map(e => ({
                    v: {
                        type: 'district',
                        name: e,
                    },
                    value, e,
                    label: e,
                }));
            }
        },
        async mounted() {
            this.selected = this.value;
            this.data = await this.loadProvince();
        },
        watch: {
            selected(val) {
                this.$emit('input', val);
            },
            async value(val) {
                this.selected = this.value;
                let [p,c] = val;
                if (!util.String.isNullOrEmpty(p) && this.data.length > 0) {
                    // Find item index of province
                    let index = this.data.map(e=>e.label).indexOf(p);
                    console.log('[AreaSelect]', '初始化选择省份: ', p, index);
                    if (index != -1) {
                        // Get city of province
                        this.data[index].children = await this.loadCity(p);
                        let indexC = this.data[index].children.map(e=>e.label).indexOf(c);
                        console.log('[AreaSelect]', '初始化选择城市: ', p, c, indexC);
                        // Get district of city
                        if (indexC != -1) {
                            this.data[index].children[indexC] = await this.loadDistrict(p, c);
                        }
                    }
                }
            }
        }
    }
</script>
<style>
    .ft { fot-size: 13.5px !important; }
</style>