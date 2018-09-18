<template>
    <!-- 客户列表 -->
    <Card class="card-margin">
        <p slot="title">
            <a v-if="selected != -1" @click="selected = -1; $emit('on-back-to-list');" href="javascript:void(0)" style="margin-right: 10px;">返回</a>
             {{ cardTitle }}
        </p>

        <div v-if="selected == -1">
            <div style="margin: 10px;">
                <Input v-model="searchKey" placeholder="搜索客户ID或客户名称" />
            </div>

            <!-- TODO: 判断权限，不对的给alert -->
            <Tabs v-model="viewRes" >
                <TabPane label="当前账号客户" name="self"></TabPane>
                <TabPane label="本组的客户" name="group" v-if="P.AdminCustomerListOfCurrentGroup"></TabPane>
                <TabPane label="全部客户" name="all" v-if="P.AdminCustomerListAll"></TabPane>
            </Tabs>

            <PagedTable
                    :columns="columns"
                    vif="typeof showList === 'undefined' || showList"
                    :data-source="dataSource"
                    @on-select="handleOnSelect"
                    :additional-params="additionalParams"
            />
        </div>

         <div v-else>
             <CustomerOverview :cus-data="cdata"
                @on-select-company="val=>$emit('on-select-company',val)"
                @on-back-to-user-list="selected = -1; $emit('on-back-to-list');"
              />
         </div>

    </Card>
</template>

<script>

/*
  选择客户组件：
    事件：
        on-select(客户所有信息)  在选择客户管理时触发 
        on-select-company(客户公司的所有信息) 在选择客户公司触发
        on-list()   在展开list时触发
        on-back-to-list()   在返回列表的时候触发
    参数：
        show-list   控制是否应该展开list (bool)
*/

import $ from '../../../js/ajax.js';
import util from '../../../js/util.js';
import { industry, receiptStatus, memberType } from '../../../constant.js';
import PagedTable from '../../pagedTable.vue';
import CustomerOverview from './customeroverview.vue';
import debounce from 'debounce';

export default {
    components: {
        PagedTable, CustomerOverview
    },
    props: [
        'showList', 'uid'
    ],
    data: () => ({
        P: window.config.P,

        searchKey: '',
        searchKeySubmit: '',
        viewRes: "self",
        selected: -1, // 选择的客户ID
        cdata: {
            // 已经选择的客户数据    
        },
        columns() {
            let self = this;
            return [
                { title:'客户名', key: 'name' },
                { title:'客户类型',render:(h,p)=>h('span',{},memberType[self.d[p.index].type]) }, // type
                { title:'行业', render:(h,p)=>h('span',{},industry[self.d[p.index].industry]) }, //industry
                { title:'手机号码', key: 'phone' },
                { 
                    title: '地址', 
                    render: (h,p)=>h('span',{}, self.d[p.index].province + self.d[p.index].city + self.d[p.index].district ), 
                },
                { 
                    title: '操作', 
                    key: 'action', 
                    width: 150,
                    render: (h, p) => {
                        return h('div', [
                            h('a', {
                                props: {
                                    href: 'javascript:void(0)',
                                },
                                on: {
                                    click: () => {
                                        let info = this.d[p.index];
                                        this.$emit('on-select', info);
                                    }
                                }
                            }, '管理')
                        ]);
                    }
                }
            ];
        }
    }),
    computed: {
        cardTitle() {
            return this.selected == -1 ? '客户列表':(`客户信息：${this.cdata.name}(${this.selected})`)
        },
        additionalParams() {
            return util.forGetParams({
                key: this.searchKeySubmit
                        .replace(/什么/g, " ")
                        .replace(/\s{2,}/g, " ")
                        .replace(/\s$/, "")
                        .replace(/^\s/, "")
            });
        },
        dataSource() {
            if (this.viewRes === "self")
                return "customer";
            return "customer/list/" + this.viewRes;
        }
    },
    methods: {
        handleOnSelect(cd) {
            this.cdata = cd;
            this.selected = cd.uid;
            this.$emit('on-select', cd);
            //alert('show info of ' + cd.name);
        }
    },
    watch: {
        searchKey: debounce(function(val) {
            this.searchKeySubmit = val;
        }, 1000),
    },
    mounted() {
        //this.
    }
}
</script>
