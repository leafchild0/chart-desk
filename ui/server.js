/**
 * Main server file
 *
 * @author vmalyshev
 * @date 15.03.2022
 */

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const routes = require('./routes');
const cors = require('cors');
//const consul = require('consul')();
//const serviceName = 'ui-service';

app.use(express.static('dist'));
// Use cors for all requests
app.use(cors());
app.use(bodyParser.json());
app.use('/api', routes);

app.get('*', (req, res, next) => {
	console.log(req.method + '' + req.path);
	next();
});

const PORT = process.env.PORT || 3003;
app.listen(PORT, () => {
	console.log(`App listening on port ${PORT}`);
	// Specify host name, using localhost for testing
	/*consul.agent.join('127.0.0.1', function(err) {
		if (err) throw err;

		consul.agent.service.register(serviceName, function(err) {
			if (err) throw err;
		});
	});*/
});

/*process.on('exit', function() {
	consul.agent.service.deregister(serviceName, function(err) {
		if (err) throw err;
	});
});*/
