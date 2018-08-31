const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const VueLoaderPlugin = require("./node_modules/vue-loader/lib/plugin")

module.exports = {
        entry: {
            //libraries: [
            //    'vue', 'jquery'
            //],
            app: "./js/index.js",
            login: "./js/login.js"
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
                {
                    test: /\.vue$/,
                    loader: 'vue-loader'
                },
                { 
                    test: /\.(eot|woff|woff2|svg|ttf)([\?]?.*)$/, 
                    loader: "file-loader?outputPath=dist/"
                 }
            ],
        },
        resolveLoader: {
            alias: {
              'vue-loader': require.resolve('./node_modules/vue-loader/lib/')
            }
        },
        plugins: [
            //new webpack.ProvidePlugin({
                //Vue: '../node_modules/vue/dist/vue.js',
            //}),
            new VueLoaderPlugin()
        ]
};