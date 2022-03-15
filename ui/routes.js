/**
 * Routes for the app, will be transferred to a gateway
 *
 * @author vmalyshev
 * @date 15.03.2022
 */
const express = require('express');
const router = express.Router();

router
	.post('/auth/login', (req, res) => {
		console.log('Login with following data', req.body);
		res.send();
	})
	.post('/auth/signup', (req, res) => {
		console.log('Sign Up with following data', req.body);
		res.send(201);
	})

module.exports = router;
