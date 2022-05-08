/**
 * Routes for the app, will be transferred to a gateway
 *
 * @author vmalyshev
 * @date 15.03.2022
 */
const express = require('express');
const router = express.Router();
const dummyToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0Ijox' +
	'NTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
const user = {
	id: 2,
	firstName: 'John',
	lastName: 'Doe',
	email: 'johndoe@email.com',
	username: 'jonnydoe',
	isAdmin: true
}
const users = [
	user,
	{
		id: 2,
		firstName: 'Judit',
		lastName: 'Ferrow',
		email: 'jferrow@email.com',
		username: 'jferrow',
		isAdmin: false
	},
	{
		id: 3,
		firstName: 'Brad',
		lastName: 'Pitt',
		email: 'bradpitt@email.com',
		username: 'bradpitt',
		isAdmin: false
	}

]

router
	.post('/auth/login', (req, res, next) => {
		console.log('Login with following data', req.body);
		res.json({accessToken: dummyToken});
		next();
	})
	.post('/auth/signup', (req, res, next) => {
		console.log('Sign Up with following data', req.body);
		res.sendStatus(201);
		next();
	})
	.get('/auth/user', (req, res, next) => {
		console.log('Get current user');
		res.json(user);
		next();
	})
	.get('/auth/users', (req, res, next) => {
		console.log('Get all users');
		res.json(users);
		next();
	})
	.put('/auth/user', (req, res, next) => {
		console.log('Update current user', req.body);
		res.json(req.body);
		next();
	})
	.post('/auth/password', (req, res, next) => {
		console.log('Update current user password', req.body);
		res.sendStatus(200);
		next();
	})
	.post('/auth/user/:id/deactivate', (req, res, next) => {
		console.log('Deactivate user with id', req.params);
		res.sendStatus(200);
		next();
	})

module.exports = router;
