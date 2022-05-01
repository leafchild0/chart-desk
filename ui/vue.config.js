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
			'^/api/proxy': {
				target: 'http://localhost:8081',
				pathRewrite: { '^/api/proxy': '/proxy' },
				ws: true,
				changeOrigin: true
			}
		}
	}
}
