<template>
    <div class="margin-top: 10px;">
        <div class="inline item">
            <div class="inline title" >
                <Poptip trigger="hover" :title="subtitle" :content="desc ? desc : '无关于该设置项的描述'"  placement="top-start">
                    <span :style="{ color: !disabled ? '#000':'#aaa' }">{{ title }}</span>
                </Poptip>
            </div>
            <div  class="inline input">
                <InputNumber v-if="type == 'int'" v-model="inputValue" placeholder="整数值" :disabled="disabled"></InputNumber>
                <InputNumber v-else-if="type == 'number'" v-model="inputValue" placeholder="数字" :disabled="disabled"/>
                <Input v-else-if="type == 'string'" v-model="inputValue" placeholder="字符串值" :disabled="disabled"/>
                <Input type="textarea" v-else-if="type == 'text'" v-model="inputValue" placeholder="文本"  :rows="4" :disabled="disabled"/>
                <DatePicker type="date" v-else-if="type == 'date'" v-model="inputValue" placeholder="选择日期" :disabled="disabled"></DatePicker>
                <Checkbox v-else-if="type == 'bool'" v-model="inputValue" :disabled="disabled"></Checkbox>
                <Select v-else-if="type == 'enum'" v-model="inputValue.value" placeholder="选择一项" :disabled="disabled">
                    <Option v-for="(e, i) in inputValue.options" :key="i" :value="i">{{e}}</Option>
                </Select>
                <Select multiple v-else-if="type == 'senum'" v-model="inputValue.value" placeholder="选择一项或多项" :disabled="disabled">
                    <Option v-for="(e, i) in inputValue.options" :key="i" :value="i">{{e}}</Option>
                </Select>
                <InputNumber v-else-if="type == 'uint'" v-model="inputValue" placeholder="无符号整数" :disabled="disabled"/>
                <Input type="password" v-else-if="type == 'password'" v-model="inputValue" :placeholder="value == ''?'密码':'安全原因，密码未予显示'" :disabled="disabled"/>
                <span v-else>{{ inputValue }}</span>
            </div>
        </div>
        <div class="inline" style="color: red">
            {{ typeErr }}
        </div>
    </div>
</template>

<script>

/**
 * 配置项
 * 
 * 事件：
 *   on-load:   配置项读取成功
 *   on-invalid-type:  未知的数据类型
 *   on-error:  更新数据项失败
 *   on-success:  更新数据项成功
 * 
 * 参数：
 *   friendlyName  设置名
 *   name          同上
 *   value         设置值
 *   type          设置类型
 *   disabled      禁用
 */

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { settingTypes } from '../../../constant.js';
import debounce from "lodash.debounce"
let _ = { debounce };
import md5 from 'js-md5';

export default {
    props: [
        'friendlyName', 'name', 'value', 'type', 'disabled', 'desc'
    ],
    data: ()=>({
        inputValue: null,
        typeErr: '',
        beforeInit: true,
        handlers: {
                int: {
                    handler: val => Number.parseInt(val),
                    validate: val => !isNaN(val),
                    defaultValue: 0,
                    stringify: val => String(val),
                },
                number: {
                    handler: val => Number(val),
                    validate: val => !isNaN(val),
                    defaultValue: 0,
                    stringify: val => String(val),
                },
                string: {
                    handler: val => val,
                    validate: val => !(val==null || typeof val == "undefined"),
                    defaultValue: '',
                    stringify: val => val,
                },
                text: {
                    handler: val => val,
                    validate: val => !(val==null || typeof val == "undefined"),
                    defaultValue: '',
                    stringify: val => val,
                },
                date: {
                    handler: val => new Date(val),
                    validate: val => !isNaN(val.getTime()),
                    defaultValue: new Date(),
                    stringify: val => (val.getYear() + 1900 + '/' + (val.getMonth() + 1) + '/' + (val.getDate())),
                },
                bool: {
                    handler: val => val != 'false',
                    validate: val => true,
                    defaultValue: false,
                    stringify: val => val ? 'true' : 'false',
                },
                enum: {
                    handler: val => {
                        if (typeof val != "string")
                            return null;
                        let [v, e] = val.split(',,');
                        if (typeof e === "undefined")
                            return null;
                        e = e.split(',');
                        v = parseInt(v);
                        if (isNaN(v))
                            v = -1;
                        return ({
                            value: v,
                            options: e,
                        });
                    },
                    validate: val => val != null,
                    defaultValue: { value: -1, options: [] },
                    stringify: val => [ '' + val.value, val.options.join(',') ].join(',,'),
                },
                senum: {
                    handler: val => {
                        if (typeof val != "string")
                            return null;
                        let [v, e] = val.split(',,');
                        if (typeof e === "undefined")
                            return null;
                        e = e.split(',');
                        v = v.split(',')
                             .map(e=>Number.parseInt(e))
                             .filter(e=>!isNaN(e));
                        return ({
                            value: v,
                            options: e,
                        });
                    },
                    validate: val => val != null,
                    defaultValue: { value: [], options: [] },
                    stringify: val => [ val.value.join(','), val.options.join(',') ].join(',,'),
                },
                uint: {
                    handler: val => Number.parseInt(val),
                    validate: val => !isNaN(val),
                    defaultValue: 0,
                    stringify: val => ''+val,
                },
                password: {
                    handler: val => '',
                    validate: val => true,
                    defaultValue: '',
                    stringify: val => md5(''+val),
                },
            },
        
    }),
    methods: {
        factory(val, ty) {
            let handlers = this.handlers;

            let h = handlers[ty];
            if (typeof h == "undefined")
                return null;
            let value = h.handler(val);
            if (!h.validate(value)) {
                //debugger;
                this.typeErr = 'Invalid type: ' + ty + ' with value: ' + val;
                console.error('[ConfigItem]', this.typeErr);
                return h.defaultValue;
            }
            return value;
        },
        async updateValue(value) {
            if (typeof value != "string") {
                console.err('[ConfigItem] value must be a string');
                return;
            } 
            try {
                let result = await $.ajax('/api/system/settings/' + this.name, {value});
                if (result.code) {
                    this.typeErr = result.msg;
                    this.$emit('on-error', value);
                }
                else {
                    this.$emit('on-success', value);
                }
            }
            catch(err) {
                this.typeErr = '无法保存设置值';
                this.$emit('on-error', value);
            }
        }
    },
    computed: {
        title() {
            if (util.isStringNullOrEmpty(this.friendlyName)) {
                return this.name;
            }
            return this.friendlyName;
        },
        subtitle() {
            return this.name;
        }
    },
    watch: {
        inputValue: {
            handler: _.debounce(function watchInputValue(val) {
                if (this.disabled) {
                    // 值是disabled的会在启动的时候请求一下  很奇怪虽然在这里禁用了，但是最好还是解决一下这个问题
                    return;
                }
                let h = this.handlers[this.type];
                if (typeof h == "undefined") return;
                //debugger;
                if (!h.validate(val)) {
                    this.inputValue = h.defaultValue;
                    return;
                }
                /*if (this.beforeInit) {
                    this.beforeInit = false;
                    return;
                }*/
                this.updateValue(h.stringify(val));
            }, 1000),
            deep: true,
        }
    },
    mounted() {
        if (typeof this.value != "undefined") {
            this.inputValue = this.factory(this.value, this.type);
            if (this.inputValue != null) {
                this.$emit('on-load', this.inputValue);
            }
            else {
                this.$emit('on-invalid-type', this.type);
            }
        }
    }
}
</script>

<style>
div.item {
    width: 500px;
    margin-left: 40px;
}

div.inline {
    display: inline-block;
}

div.title {
    width: 190px;
    margin-right: 10px;
}

div.subtitle {
    font-size: 12px;
    color: gray;
}

div.input {
    width: 220px;
}

div.input>* {
    width: 220px;
}

    .top,.bottom{
        text-align: center;
    }
    .center{
        width: 300px;
        margin: 10px auto;
        overflow: hidden;
    }
    .center-left{
        float: left;
    }
    .center-right{
        float: right;
    }

</style>
