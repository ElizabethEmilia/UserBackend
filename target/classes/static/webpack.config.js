const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
        entry: {
            libraries: [
                'vue',
            ],
            app: "./js/index.js"
        },
        output: {
            path: __dirname + "/dist",
            filename: "[name].bundle.js"
        },
        mode: 'development',
        devtool: 'none',
        module: {
            rules: [
                {
                    test: /(\.jsx|\.js)$/,
                    use: {
                        loader: 'babel-loader',
                        options: {
                            presets: ['es2015']
                        }
                    },
                    exclude: /(node_modules|bower_components)/
                },
                {
                    test: /\.less$/,
                    use: [{
                        loader: "style-loader"
                    }, {
                        loader: "css-loader"
                    }, {
                        loader: "less-loader"
                    }]
                },
                {
                    test: /\.css$/,
                    use: [{
                        loader: "style-loader"
                    }, {
                        loader: "css-loader"
                    }]
                },
                
            ]
        },
        plugins: [
            new webpack.ProvidePlugin({
                $: 'jquery',
                jQuery: 'jquery',
                'window.jQuery': 'jquery',
                Vue: '../node_modules/vue/dist/vue.js',
            }),
        ]
};