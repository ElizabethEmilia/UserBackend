<template>
    <div style="display: inline-block">
        <Select v-model="selected[0]" style="width: 100px;" placeholder="省/市/自治区">
            <Option v-for="(e,i) in provinces" :value="e" :key="i">{{ e }}</Option>
        </Select>
        <Select v-model="selected[1]" style="width: 100px;" :placeholder="isDirectAdministratedCities?'县/区/市':'地级市'">
            <Option v-for="(e,i) in cities" :value="e" :key="i">{{ e }}</Option>
        </Select>
        <Select v-model="selected[2]" style="width: 100px;" :disabled="isDirectAdministratedCities" :placeholder="isDirectAdministratedCities?'':'县/区/市'">
            <Option v-for="(e,i) in districts" :value="e" :key="i">{{ e }}</Option>
        </Select>
    </div>
</template>
<script>
    import API from '../../js/api.js';
    import util from '../../js/util.js';

    export default {
        props: [ 'value' ],
        data () {
            return {
                selected: [],
                provinces: [],
                cities: [],
                districts: [],
                init: true,
            }
        },
        methods: {
            async loadProvince() {
                let result = await API.Area.getProvince();
                return result;
            },
            async loadCity(province) {
                let result = await API.Area.getCity(province);
                return result;
            },
            async loadDistrict(province, city) {
                let result = await API.Area.getDistruct(province, city);
                return result;
            }
        },
        async mounted() {
            this.provinces = await this.loadProvince();
            this.selected = this.value;
        },
        computed: {
            selectedProvince() { return this.selected[0]; },
            selectedCity() { return this.selected[1]; },

            isDirectAdministratedCities() {
                return ['北京','天津','上海','重庆','台湾','香港','澳门'].indexOf(this.selectedProvince) >= 0;
            }
        },
        watch: {
            selected(val) {
                this.$emit('input', val);
            },
            async value(val) {
                if (!this.init) return;
                this.selected = this.value;
                let p = this.value[0];
                let c = this.value[1];
                //this.provinces.push(this.value[0]);
                this.cities.push(this.value[1]);
                this.districts.push(this.value[2]);

                if (!util.String.isNullOrEmpty(p) && this.provinces.length > 0) {
                    // Find item index of province
                    let index = this.provinces.indexOf(p);
                    console.log('[AreaSelect]', '初始化选择省份: ', p, index);
                    if (index != -1) {
                        // Get city of province
                        this.cities = await this.loadCity(p);
                        let indexC = this.cities.indexOf(c);
                        console.log('[AreaSelect]', '初始化选择城市: ', p, c, indexC);
                        // Get district of city
                        if (indexC != -1) {
                            this.districts = await this.loadDistrict(p, c);
                        }
                    }
                }

                this.init = false;

            },
            async selectedProvince(p) {
                if (this.init) return;
                this.districts = [];
                this.cities = [];
                this.selected[1] = this.selected[2] = "";
                this.cities = await this.loadCity(p);
            },
            async selectedCity(c) {
                if (this.init) return;
                this.selected[2] = "";
                this.districts = [];
                this.districts = await this.loadDistrict(this.selectedProvince, c);
            }
        }
    }
</script>
<style>
    .ft { fot-size: 13.5px !important; }
</style>