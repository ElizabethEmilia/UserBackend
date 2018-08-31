import Vue from 'vue';
import iView from 'iview';
import '../node_modules/iview/dist/styles/iview.css';
import LoginPage from '../vue/loginpage.vue';

Vue.use(iView);

new Vue({
	el: '#app',
    render: h => h(LoginPage),
});