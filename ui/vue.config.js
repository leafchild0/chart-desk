module.exports = {
	devServer: {
		proxy: {
			'^/api/auth': {
				target: 'http://localhost:3003',
				ws: true,
				changeOrigin: true
			},
			'^/api/charts': {
				target: 'http://localhost:8081',
				pathRewrite: { '^/api/charts': '/charts' },
				ws: true,
				changeOrigin: true
			},
			'^/api/tags': {
				target: 'http://localhost:8081',
				pathRewrite: { '^/api/tags': '/tags' },
				ws: true,
				changeOrigin: true
			}
		}
	}
}
