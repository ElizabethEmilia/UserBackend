require('../css/style.less');

const VersionInfo = require("../package.json");

console.log(`${VersionInfo.description} (Ver${VersionInfo.version})`);

new Vue({
	el: '#app',
	data: {
        VersionInfo,
    }
});