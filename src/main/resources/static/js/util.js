function forPostParams(obj) {
	if (typeof obj !== "object")
		throw new Error("obj is not an object");
	return encodeURIComponent(Object.keys(obj).map(e=>e + '=' + obj[e]).join('&'));
}

function forGetParams(obj) {
	if (typeof obj !== "object")
		throw new Error("obj is not an object");
	return encodeURI(Object.keys(obj).map(e=>e + '=' + obj[e]).join('&'));
}

function forGetURL(url, obj) {
    return encodeURI(url+forGetParams(obj));
}

export default {
    forGetParams, // 通过对象生成Get方法参数
    forPostParams,  // 通过对象生成Post方法参数
    forGetURL,    // 通过对象生成Get方法地址
}