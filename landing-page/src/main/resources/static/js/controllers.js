(function() {

	'use strict';

	/* controllers Module */

	var moduleCntrl = angular.module('controllers', [ 'ui.bootstrap' ]);

	moduleCntrl.controller('controller', function($rootScope, $scope, $http, $state) {

		$scope.init = function() {
			$scope.x = 'initialized angular controller succeded';
		}
	});

	moduleCntrl.controller('headerCntrl', function($rootScope, $scope, $http, $state) {
	});

	moduleCntrl.controller('homeCntrl', function($rootScope, $scope, $http, $state) {

		$scope.register = function() {
			$state.go('register');
		}
	});

	moduleCntrl.controller('registerCntrl', function($rootScope, $scope, $http, $state) {

		$scope.model = {};

		$scope.register = function() {
			if (validateFields()) {
				let url = "http://localhost:8082/register";
				let headers = {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
				$http({
					method : 'POST',
					data : $scope.model,
					url : url,
					headers : headers
				}).success(function(data) {
					$scope.model = {};
					alert("Registration completed!");
				}).error(function(data) {
					alert('ERROR: something went wrong!! please try again.');
				});
			}
		};

		function validateFields() {
			if ($scope.model.firstName == null || $scope.model.firstName == undefined) {
				alert('you must enter first name!');
				return false;
			}
			if ($scope.model.lastName == null || $scope.model.lastName == undefined) {
				alert('you must enter last name!');
				return false;
			}
			if ($scope.model.email == null || $scope.model.email == undefined) {
				alert('you must enter a valid email!');
				return false;
			}
			if ($scope.model.country == null || $scope.model.country == undefined) {
				alert('you must enter country!');
				return false;
			}
			if ($scope.model.phone == null || $scope.model.phone == undefined) {
				alert('you must enter phone number!');
				return false;
			}
			return true;
		}
	});

	moduleCntrl.controller('footerCntrl', function($rootScope, $scope, $http, $state) {
	});

})();