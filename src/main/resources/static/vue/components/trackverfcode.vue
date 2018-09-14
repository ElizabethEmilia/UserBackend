<template>
    <div style="z-index: 99999">
        <div class="vercode-track" ref="track">
            <p ref="tips" class="tips-inside-track">{{ trackText }}</p>
            <div @click="loadImage" class="vercode-img default-hide" ref="img" :style="{ background: !image ? '#eee':'url(data:image/png;base64,' + image + ')' }">
            </div>
            <div style="position: relative">
                <div class="vercode-track-selected" ref="selected">{{ selectedBarText }}</div>
                <div class="vercode-button" ref="slider">
                    <div  style="position: relative">
                        <div class="vercode-slider  default-hide" :style="{ background: !slider ? '#eee':'url(data:image/png;base64,' + slider + ')' }"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import API from '../../js/api.js';

    export default {
        data: () => ({
            image: null,
            slider: null,

            clicked: false,
            startClickX: -1,
            lastMoveX: 0,

            selectedBarText: '',
            trackText: '拖动滑块以拼图',
            errorOccurred: false,

            pendingVerify: false,
        }),
        methods: {
            async loadImage() {
                try {
                    let r = await API.Verification.getImages();
                    this.image = r.bg;
                    this.slider = r.slider;
                }
                catch (e) {
                    console.error(e);
                }
            },
            verifyOK() {
                let slider = this.$refs.slider;
                let img =this.$refs.img;
                let selected = this.$refs.selected;

                slider.style.display = img.style.display = 'none';
                selected.style.width = "100%";
                this.selectedBarText = '验证通过';
                this.$emit("on-verifyok", true);
            },
            verifyFailed() {
                let slider = this.$refs.slider;
                let track =this.$refs.track;
                let selected = this.$refs.selected;
                let tips = this.$refs.tips;

                slider.style.left = '0px';
                selected.style.width = '0px';
                track.style.background = 'red';
                tips.style.color = '#fff';
                this.trackText = "验证未通过，请重试";
                this.errorOccurred = true;
                this.loadImage();
            },
            resoreErrorState() {
                let slider = this.$refs.slider;
                let track =this.$refs.track;
                let selected = this.$refs.selected;
                let tips = this.$refs.tips;

                track.style.background = '#ddd';
                this.trackText = "拖动滑块以拼图";
                tips.style.color = '#888';
                this.errorOccurred = false;
            },
            async verify(x) {
                this.pendingVerify = true;
                console.log('[当前位置]', x);
                try {
                    await API.Verification.verify(x);
                    this.pendingVerify = false;
                    this.verifyOK();
                }
                catch (e) {
                    this.pendingVerify = false;
                    console.error(e);
                    this.verifyFailed();
                }
            }
        },
        mounted() {
            this.loadImage();

            let slider = this.$refs.slider;
            let track =this.$refs.track;
            let img =this.$refs.img;
            let selected = this.$refs.selected;
            let tips = this.$refs.tips;

            function limit(x, min, max) {
                if (x < min) return 0;
                if (x > max) return max;
                return x;
            }

            var startx = 0;

            slider.addEventListener('mousedown', (e) => {
                /*this.clicked = true;
                if (this.startClickX < 0)
                    this.startClickX = e.clientX;*/
                if (this.pendingVerify)
                    return;
                if (this.errorOccurred)
                    this.resoreErrorState();

                var event=event||window.event;
                var _target = event.target
                startx=event.clientX;
                var starty=event.clientY;
                var sb_bkx=startx-event.target.offsetLeft;
                var sb_bky=starty-event.target.offsetTop;
                var ww=document.documentElement.clientWidth;
                var wh = window.innerHeight;
                if (event.preventDefault) {
                    event.preventDefault();
                } else{
                    event.returnValue=false;
                };
                document.onmousemove = (ev) => {
                    var event=ev||window.event;
                    var scrolltop=document.documentElement.scrollTop||document.body.scrollTop;
                    if (event.clientY < 0 || event.clientX < 0 || event.clientY > wh || event.clientX > ww) {
                        return false;
                    };
                    var endx=event.clientX-sb_bkx;
                    var endy=event.clientY-sb_bky;
                    endx = limit(endx, 0, 245);
                    console.log(endx);
                    slider.style.left=endx+'px';
                    selected.style.width=(endx+25)+'px';
                }
            });
            document.body.addEventListener('mouseup', (e) => {
                if (document.onmousemove) {
                    document.onmousemove=null;
                    this.verify(e.clientX - startx);
                }

            });

        }
    }
</script>

<style scoped>
    h1              { font-size: 2em; margin: .67em 0 }
    h2              { font-size: 1.5em; margin: .75em 0 }
    h3              { font-size: 1.17em; margin: .83em 0 }
    h4, p,
    blockquote, ul,
    fieldset, form,
    ol, dl, dir,
    menu            { margin: 1.12em 0 }
    h5              { font-size: .83em; margin: 1.5em 0 }
    h6              { font-size: .75em; margin: 1.67em 0 }
    h1, h2, h3, h4,
    h5, h6, b,
    strong          { font-weight: bolder }
    blockquote      { margin-left: 40px; margin-right: 40px }
    i, cite, em,
    var, address    { font-style: italic }
    pre, tt, code,
    kbd, samp       { font-family: monospace }
    pre             { white-space: pre }
    button, textarea,
    input, select   { display: inline-block }
    big             { font-size: 1.17em }
    small, sub, sup { font-size: .83em }
    sub             { vertical-align: sub }
    sup             { vertical-align: super }
    table           { border-spacing: 2px; }
    thead, tbody,
    tfoot           { vertical-align: middle }
    td, th, tr      { vertical-align: inherit }
    s, strike, del  { text-decoration: line-through }
    hr              { border: 1px inset }
    ol, ul, dir,
    menu, dd        { margin-left: 40px }
    ol              { list-style-type: decimal }
    ol ul, ul ol,
    ul ul, ol ol    { margin-top: 0; margin-bottom: 0 }
    u, ins          { text-decoration: underline }
    br:before       { content: "\A"; white-space: pre-line }
    center          { text-align: center }
    :link, :visited { text-decoration: underline }
    :focus          { outline: thin dotted invert }

    .default-hide {
        opacity: 0;
        transition: opacity 0.2s;
        visibility: hidden;
    }

    .vercode-img {
        position: absolute;
        bottom: 40px;
        width: 300px;
        height: 150px;
        border-radius: 10px;
        z-index: 99999;
        box-shadow: 0px 0px 3px 3px rgba(0,0,0,0.3);
    }
    .vercode-track {
        margin: 10px 5px 10px 5px;
        width: 290px; height: 30px;
        border-radius: 15px;
        background: #ddd;
        position: relative;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        z-index: 99999;
        transition: background-color 0.1s, background 0.1s;
    }
    .tips-inside-track {
        position: absolute;
        top: -5px;
        font-size: 12px;
        color: #888;
        width: 100%;
        text-align: center;
    }
    .vercode-track:hover .default-hide {
        opacity: 1;
        visibility: visible;
    }
    .vercode-track-selected {
        border-radius: 15px;
        height: 30px;
        width: 0px;
        position: absolute;
        left: 0px;
        top: 0px;
        background: lightgreen;
        font-size: 12px;
        text-align: center;
        line-height: 30px;
        color: #111;
    }
    .vercode-button {
        position: absolute;
        height: 40px;
        width: 40px;
        top: -5px;
        left: -5px;
        -webkit-border-radius: 50%;
        -moz-border-radius: 50%;
        border-radius: 50%;
        background: #999;
        transition: margin-left 0.2s;
    }
    .vercode-button:hover {
        background: #aaa;
    }
    .vercode-button:active {
        background: #888;
    }
    .vercode-slider {
        position: absolute;
        height: 50px;
        width: 50px;
        bottom: 15px;
        filter: drop-shadow(0 0 2px #999) drop-shadow(0 0 10px #666) drop-shadow(0 0 5px #111);
        z-index: 100000;
    }
</style>