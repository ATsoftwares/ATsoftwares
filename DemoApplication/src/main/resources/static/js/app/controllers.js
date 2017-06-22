/**
 * controllers
 */

app.controller('headerCntrl', function($scope, $http, $demoHttp, $idleTimeout, $cookies, $rootScope, $state) {

	$rootScope.$on("login:authorized", function(e, value) {
		$scope.username = value;
	});

	if ($cookies.get('loggedInUser') != undefined) {
		$scope.username = $cookies.get('loggedInUser');
	} else {
		$idleTimeout.stop();
		alert('user is not authorized');
		$state.go('login');
	}

	$scope.logOut = function() {
		$http.post('/app/logout').then(function() {
			$idleTimeout.stop;
			$cookies.remove("loggedInUser", {
				path : '/'
			});
			$state.go('login');
		});
	};

});

app.controller('homeCntrl', function($scope, $http, $demoHttp, $cookies, $rootScope, $state) {

	$scope.click = function() {
		alert("hello");
	}
});

app.controller('registerCntrl', function($scope, $http, $demoHttp, $cookies, $rootScope, $state) {

	$scope.register = function(user, password) {
		console.log('register');
	}
});

app.controller('calanderCntrl', function($scope, $http, $demoHttp, $cookies, $rootScope, $state) {

	$scope.model = {};
	$scope.model.name = "initiatlized";
	/* $scope.model.date1 = 1492629107829; */

	$scope.foo = function() {
		$scope.model.cc = "xxx";
	}

	// return an array of all datepicker fields names
	function initDatePickerFields() {
		$scope.DatePickerArrayFields = [];
		var datePickerElements = $('.datepickerInput');
		for (var i = 0; i < datePickerElements.length; i++) {
			$scope.DatePickerArrayFields.push(datePickerElements[i].name);
		}
		return $scope.DatePickerArrayFields;
	}

	// check if a date value has changed
	function checkDateChanged(newValue, oldValue) {
		for (var i = 0; i < oldValue.length; i++) {
			if (oldValue[i] != newValue[i] && newValue[i] != undefined) {
				if (!isDateValid(newValue[i])) {
					$("input[name='" + $scope.DatePickerArrayFields[i] + "']").addClass('input-error');
				} else {
					$("input[name='" + $scope.DatePickerArrayFields[i] + "']").removeClass('input-error');
				}
			}
		}

	}

	// checks if a string is a valid date, assuming the format is dd/MM/yyyy
	function isDateValid(value) {
		if (value.length < 10) {
			return false;
		} else {
			if ((new Date(value)) != 'Invalid Date') {
				return true;
			}
			return false;
		}
	}

	// trigger $watch event on all date fields when they change
	$scope.$watchGroup(initDatePickerFields(), function(newValue, oldValue) {
		checkDateChanged(newValue, oldValue)
	});

});

app.controller('loginCntrl', function($scope, $demoHttp, $http, $cookies, $rootScope, $state, $idleTimeout) {

	$scope.login = function(user, password) {
		if (user == undefined) {
			alert("user not entered");
			return;
		}
		if (password == undefined) {
			alert("password not entered");
			return;
		}
		var credentials = {
			"user" : user,
			"password" : password
		}

		var header = credentials ? {
			Authorization : "Basic " + btoa(credentials.user + ":" + credentials.password)
		} : {};

		$scope.refresh = function() {
			$http.get('/app/login', {
				headers : header
			}).then(function(response) {
				if (response.status == "200") {
					var $expiration = new Date();
					var minutes = 5;
					$expiration.setTime($expiration.getTime() + minutes * 60 * 1000)
					$cookies.put('loggedInUser', $scope.user, {
						expires : $expiration,
						path : '/'
					});
					$rootScope.$broadcast("login:authorized", $scope.user);
					if ($scope.user != undefined) {
						$idleTimeout.start();
						$state.go('home');
					}
				}
			});
		};
		$scope.refresh();
	};

});