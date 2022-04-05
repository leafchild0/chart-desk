module.exports = {
	devServer: {
		proxy: {
			'^/api': {
				target: 'http://localhost:3003',
				ws: true,
				changeOrigin: true
			},
			'^/charts_api': {
				target: 'http://localhost:8081',
				pathRewrite: { '^/charts_api': '' },
				ws: true,
				changeOrigin: true
			}
		}
	}
}
