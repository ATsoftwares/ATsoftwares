(function() {

	'use strict';

	/* App Module */

	var app = angular.module("app", [ 'controllers', 'ui.router']).run(
			[ '$rootScope', '$state', function($rootScope, $state) {

				$rootScope.check = "rootScope is checkd !";

			} ]);

	app.config(function($stateProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise("/home");

		var header = {
			templateUrl : "header.html",
			controller : "headerCntrl"
		};

		var footer = {
			templateUrl : "footer.html",
			controller : "footerCntrl"
		};

		addRout('home', '/home', 'homebody');

		addRout('about', '/about', 'about');

		addRout('activities', '/activities', 'activities');

		addRout('contact', '/contact', 'contact');

		function addRout(state, url, template) {
			$stateProvider.state(state, {
				url : url,
				views : {
					"header" : header,
					"footer" : footer,
					"" : {
						templateUrl : template + ".html",
						controller : template + "Cntrl"
					}
				}
			});
		}
		;
	});

})();