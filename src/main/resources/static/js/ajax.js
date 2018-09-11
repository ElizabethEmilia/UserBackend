import $ from './jquery';
import util from './util.js';

const ajax = function(url, data = undefined, contentType = undefined, progress = undefined) {
    if (typeof data === "object") {
        if (Object.keys(data).length >= 3)
            contentType='application/json';
        else
            contentType='application/x-www-form-urlencoded';
        data = util.forPostParams(data);
    }
    return new Promise((resolve, reject) => {
        $.ajax({
            url: encodeURI(url),
            dataType: 'json',
            data: data,
            type: typeof data === "undefined" ? 'GET':'POST',
            contentType: contentType,
            success: (data)=> {
                resolve(data);
            },
            error: (err)=> {
                console.error(`${ typeof data === "undefined" ? 'GET':'POST' }/ ${url} failed`);
                reject(new Error(err));
            },
            progress: progress
        });
    });
};

const DELETE = function(url) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: encodeURI(url),
            dataType: 'json',
            type: 'DELETE',
            success: (data)=> {
                resolve(data);
            },
            error: (err)=> {
                console.error(`DELETE/ ${url} failed`);
                reject(new Error(err));
            },
        });
    });
}

export default {
    ajax,
    DELETE,
};