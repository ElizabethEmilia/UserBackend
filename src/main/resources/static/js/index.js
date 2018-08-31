import Vue from 'vue';
import MainPage from '../vue/mainpage.vue';
import iView from 'iview';
import '../node_modules/iview/dist/styles/iview.css';
import VueRouter from 'vue-router';
import Routers from '../vue/routes';

Vue.use(iView);
Vue.use(VueRouter);

const RouterConfig = {
    routes: Routers
};

const router = new VueRouter(RouterConfig);

new Vue({
	el: '#app',
    render: h => h(MainPage),
    router: router
});