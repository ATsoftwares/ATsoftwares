(function() {

	'use strict';

	/* controllers Module */

	var moduleCntrl = angular.module('controllers',[]);  /* , [ 'ui.bootstrap' ]*/

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

	moduleCntrl.controller('homebodyCntrl', function($rootScope, $scope, $http, $state) {

		$scope.alert = function() {
			alert("good !");
		};
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