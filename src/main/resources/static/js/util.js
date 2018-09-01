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
    return encodeURI(url+'?'+forGetParams(obj));
}

function getQueryParameter(param) {
    let query = window.location.search.substring(1);
	let vars = query.split("&");
	for (let i=0; i<vars.length; i++) {
		let pair = vars[i].split("=");
		if (pair[0] == param)
			return pair[1];
	}
    return null;
}

function isStringNullOrEmpty(str) {
	return !(typeof str === "string" &&
		   !( typeof str === "undefined" || str === null || str.length === 0 ));
}

const ASCII = {
	_0: '0'.charCodeAt(),
	_9: '9'.charCodeAt(),
	a: 'a'.charCodeAt(),
	z: 'z'.charCodeAt(),
	A: 'A'.charCodeAt(),
	Z: 'Z'.charCodeAt(),
}

function isLetter(ch) {	
	let c = ch[0].charCodeAt();
	return (c >= ASCII.a && c <= ASCII.z) || (c >= ASCII.A && c <= ASCII.Z); 
}

function toASCIICode(ch) {
	return ch[0].charCodeAt();
}

function isLowerCase(ch) {
	let c = ch[0].charCodeAt();
	return (c >= ASCII.a && c <= ASCII.z); 
}

function isUpperCase(ch) {
	let c = ch[0].charCodeAt();
	return (c >= ASCII.A && c <= ASCII.Z); 
}

function isNumber(ch) {
	let c = ch[0].charCodeAt();
	return (c >= ASCII._0 && c <= ASCII._9); 
}

function passwordMatchesRestriction(pwd) {
	if (isStringNullOrEmpty(pwd))
		return false;
	// 验证长度
	if (pwd.length < 8)
		return false;
	// 验证符号类型
	var ty = Array(4).fill(0);
	const LOWER_CASE = 0, UPPER_CASE = 1, NUMBER = 2, SIGNS = 3;
	for (let i in pwd) {
		let ch = pwd[i];
		if (isLowerCase(ch))
			ty[LOWER_CASE] = 1;
		else if (isUpperCase(ch))
			ty[UPPER_CASE] = 1;
		else if (isNumber(ch))
			ty[NUMBER] = 1;
		else ty[SIGNS] = 1;
	}
	return ty.reduce((a,b)=>a+b) >= 2;
}

export default {
    forGetParams, // 通过对象生成Get方法参数
    forPostParams,  // 通过对象生成Post方法参数
	forGetURL,    // 通过对象生成Get方法地址
	getQueryParameter, // 获取页面参数
	isStringNullOrEmpty, // 测试字符串是否为空
	passwordMatchesRestriction, // 判断密码是否符合复杂性要求

	// 字符相关
	Character: {
		isUpperCase,
		isLowerCase,
		isNumber,
		toASCIICode,
	}
}