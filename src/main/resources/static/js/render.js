
/*
   config = [
        {
            "text": "xxx", "onclick": function() {  ... }
        }
   ]
 */
function renderLinkGroup(h, p, config) {
    return config.map(e => renderLink(h, p, e.text, e.onclick));
}

function renderLink(h, p, text, onclick) {
    return h('a', {
        props: {
            href: 'javascript:void(0)',
        },
        style: {
            marginRight: '10px',
        },
        on: {
            click() {
                onclick(p);
            }
        }
    }, text);
}

export default {
    link: renderLink,
    linkGroup: renderLinkGroup,
}