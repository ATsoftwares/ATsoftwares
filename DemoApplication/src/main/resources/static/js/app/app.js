var app = angular.module('myApp', [ 'ui.router', 'ngCookies', 'ui.bootstrap' ]).run(
		[ '$demoHttp', '$rootScope', '$idleTimeout', function($demoHttp, $rootScope, $idleTimeout) {

			$rootScope.appName = "demo";

		} ]);

app.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.interceptors.push('XSRFInterceptor');

	$urlRouterProvider.otherwise("/home");

	var header = {
		templateUrl : "views/partials/header.html",
		controller : "headerCntrl"
	};

	addRout('home', '/home', 'home');
	addRout('calander', '/calander', 'calander');
	addRout('register', '/register', 'register');

	$stateProvider.state('login', {
		url : '/login',
		templateUrl : "views/login.html",
		controller : "loginCntrl"
	});

	function addRout(state, url, template) {
		$stateProvider.state(state, {
			url : url,
			views : {
				"header" : header,
				"" : {
					templateUrl : "views/" + template + ".html",
					controller : template + "Cntrl"
				}
			}
		});
	}
});