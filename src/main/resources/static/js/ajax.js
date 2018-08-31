import $ from 'jquery';

const ajax = function(url, data = undefined, contentType = undefined, progress = undefined) {
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
}

export default {
    ajax
};