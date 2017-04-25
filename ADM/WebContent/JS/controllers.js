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

		$scope.selected = function(number) {
			$scope.select = number;
		};
	});

	moduleCntrl.controller('pricesCntrl', function($rootScope, $scope, $http, $state, $timeout, NgMap) {

		$scope.model = {};
		$scope.model.map = {};

		NgMap.getMap().then(function(map) {
			$scope.map = map;
			$scope.marker = map.markers[0];

			$scope.model.map = {
				center : {
					lat : $scope.countries[0].center.latitude,
					lng : $scope.countries[0].center.longitude
				},
				zoom : $scope.countries[0].zoom
			};
		});

		$scope.countries = [ {
			id : 1,
			name : "Israel",
			price : "1200$",
			center : {
				latitude : 31.046051,
				longitude : 34.851612
			},
			type : "onshore",
			zoom : 7
		}, {
			id : 2,
			name : "USA",
			price : "1500$",
			center : {
				latitude : 37.090240,
				longitude : -95.712891
			},
			type : "onshore",
			zoom : 4
		}, {
			id : 3,
			name : "UK",
			price : "1800$",
			center : {
				latitude : 55.378051,
				longitude : -3.435973
			},
			type : "onshore",
			zoom : 5
		}, {
			id : 5,
			name : "France",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "onshore",
			zoom : 5
		},
		// offshore countries
		{
			id : 5,
			name : "Saint Vincent & the Grenadines",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Marshal Islands",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Dominica",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Belize",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Curacao",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Panama",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Costa Rica",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Anguilla",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Seychelles",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "British Virgin Islands",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		}, {
			id : 5,
			name : "Vanuatu",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "offshore",
			zoom : 5
		} ];

		/*
		 * Cyprus United Kingdom Scotland Switzerland Ireland Malta Gibraltar Bulgaria Czech Republic Latvia Estonia Albania Serbia
		 * Montenegro Georgia Russia Poland
		 * 
		 * Far East and Pacific
		 * 
		 * Hong Kong Singapore New Zeeland U.A.E Israel
		 * 
		 */

		$scope.shores = [ {
			id : 0,
			value : "ALL"
		}, {
			id : 1,
			value : "Onshore"
		}, {
			id : 2,
			value : "Offshore"
		} ];

		$scope.areas = [ {
			value : "ALL",
			id : "0"
		}, {
			value : "America",
			id : "1"
		}, {
			value : "Europe",
			id : "2"
		}, {
			value : "Afrirca",
			id : "3"
		}, {
			value : "Far East and Pacific",
			id : "4"
		} ];

		$scope.amounts = [ {
			value : "ALL",
			id : 0
		}, {
			value : "1000$",
			id : 1
		}, {
			value : "1500$",
			id : 2
		} ];

		$scope.model.area = "0";
		$scope.model.amount = 0;
		$scope.model.isShore = 0;

		$scope.countryClick = function(country) {
			$scope.model.map.center = {
				lat : country.center.latitude,
				lng : country.center.longitude
			}

			$scope.map.setCenter($scope.model.map.center);
			$scope.map.setZoom(country.zoom);
			$scope.marker.setPosition($scope.model.map.center);
		};

		$scope.centerChanged = function(event) {
			$timeout(function() {
				$scope.map.panTo({
					lat : $scope.model.map.center.lat,
					lng : $scope.model.map.center.lng
				});
			}, 1000);
		}

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