(function() {

	'use strict';

	/* controllers Module */

	var moduleCntrl = angular.module('controllers', [ /* 'ui.bootstrap' */]);

	moduleCntrl.controller('controller', function($rootScope, $scope, $http, $state) {

		$scope.init = function() {
			$scope.x = 'initialized angular controller succeded';
		}
	});

	moduleCntrl.controller('headerCntrl', function($rootScope, $scope, $http, $state) {

		$scope.selected = function(number) {
			$scope.select = number;
		};
	});

	moduleCntrl.controller('pricesCntrl', function($rootScope, $scope, $http, $state) {

		$scope.countries = [ {
			id : 1,
			name : "Israel"
		}, {
			id : 2,
			name : "USA"
		}, {
			id : 3,
			name : "UK"
		}, {
			id : 1,
			name : "France"
		} ];

		$scope.shores = [ {
			id : 1,
			value : "onshore"
		}, {
			id : 2,
			value : "offshore"
		} ]

		$scope.areas = [ {
			value : "USA",
			id : 1
		}, {
			value : "Europe",
			id : 2
		}, {
			value : "Russia",
			id : 3
		}, {
			value : "Afrirca",
			id : 4
		} ];

		$scope.amounts = [ {
			value : "1000$",
			id : 1
		}, {
			value : "1500$",
			id : 2
		} ];
	});

	moduleCntrl.controller('homebodyCntrl', function($rootScope, $scope, $http, $state) {

		$scope.serviceBox = [ {
			imagePath : "images/city.jpg",
			imageText : "Company formation and administration",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle"
		}, {
			imagePath : "images/shake.jpg",
			imageText : "Trust formation and administration",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle"
		}, {
			imagePath : "images/consulting.jpg",
			imageText : "Consoulting Business Operations And Legal Services",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle2"
		}, {
			imagePath : "images/shareholders2.jpg",
			imageText : "Dierctors Shareholders Services",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle2 width-350"
		}, {
			imagePath : "images/financial.jpg",
			imageText : "Financial Operations",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle2"
		}, {
			imagePath : "images/account3.jpg",
			imageText : "Bank Account Operations",
			textCss : "text-center text-20 text-bold",
			middleCss : "middle2 width-250"
		} ];
	});

	moduleCntrl.controller('aboutCntrl', function($rootScope, $scope, $http, $state) {
	});

	moduleCntrl.controller('footerCntrl', function($rootScope, $scope, $http, $state) {
	});

	moduleCntrl.controller('contactCntrl', function($rootScope, $scope, $http, $state) {

		$scope.send = function() {
			alert('sent to email');
		}
	});

	moduleCntrl.controller('activitiesCntrl', function($rootScope, $scope, $http, $state) {

		$scope.add = function() {
			$scope.activity = "activity";
		};

		$scope.check = $rootScope.check;
	});

})();