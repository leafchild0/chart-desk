module.exports = {
	devServer: {
		proxy: {
			'http://localhost:8765/auth': {
				target: 'http://localhost:9091',
				pathRewrite: { '^/auth': '/' },
				ws: true,
				changeOrigin: true
			},
			'http://localhost:8765/charts': {
				target: 'http://localhost:8081',
				pathRewrite: { '^/charts': '/' },
				ws: true,
				changeOrigin: true
			},
		}
	}
}
