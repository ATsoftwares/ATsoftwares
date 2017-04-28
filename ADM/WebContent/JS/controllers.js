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

	moduleCntrl.controller('pricesCntrl', function($rootScope, $scope, $http, $state, $timeout, $filter, NgMap) {

		$scope.model = {};
		$scope.model.map = {};
		$scope.search = {};

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

			$scope.selectedCountry = $scope.countries[0].name;
		});

		// America Europe Afrirca Far East and Pacific
		$scope.countries = [
		// Onshore countries
		{
			id : 1,
			name : "Israel",
			area : "Asia",
			price : "1200$",
			center : {
				latitude : 31.046051,
				longitude : 34.851612
			},
			type : "Onshore",
			zoom : 7
		}, {
			id : 2,
			name : "USA",
			area : "America",
			price : "1500$",
			center : {
				latitude : 37.090240,
				longitude : -95.712891
			},
			type : "Onshore",
			zoom : 4
		}, {
			id : 3,
			name : "UK",
			area : "Europe",
			price : "1800$",
			center : {
				latitude : 55.378051,
				longitude : -3.435973
			},
			type : "Onshore",
			zoom : 5
		}, {
			id : 5,
			name : "France",
			area : "Europe",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "Onshore",
			zoom : 5
		},
		// Offshore countries
		{
			id : 5,
			name : "Saint Vin. & the Grenad.",
			area : "America",
			price : "1300$",
			center : {
				latitude : 13.252818,
				longitude : -61.197163
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "Marshal Islands",
			area : "America",
			price : "1300$",
			center : {
				latitude : 11.324691,
				longitude : 166.841742
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Dominica",
			area : "America",
			price : "1300$",
			center : {
				latitude : 15.414999,
				longitude : -61.370976
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Belize",
			area : "America",
			price : "1300$",
			center : {
				latitude : 17.189877,
				longitude : -88.497650
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Curacao",
			area : "America",
			price : "1300$",
			center : {
				latitude : 12.169570,
				longitude : -68.990020
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Panama",
			area : "America",
			price : "1300$",
			center : {
				latitude : 8.537981,
				longitude : -80.782127
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Costa Rica",
			area : "America",
			price : "1300$",
			center : {
				latitude : 9.748917,
				longitude : -83.753428
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Anguilla",
			area : "America",
			price : "1300$",
			center : {
				latitude : 18.220554,
				longitude : -63.068615
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "Seychelles",
			area : "Africa",
			price : "1300$",
			center : {
				latitude : -4.679574,
				longitude : 55.491977
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "British Virgin Islands",
			area : "America",
			price : "1300$",
			center : {
				latitude : 18.420695,
				longitude : -64.639968
			},
			type : "Offshore",
			zoom : 7
		}, {
			id : 5,
			name : "Vanuatu",
			area : "Far East and Pacific",
			price : "1300$",
			center : {
				latitude : -15.376706,
				longitude : 166.959158
			},
			type : "Offshore",
			zoom : 7
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
		}, {
			value : "Asia",
			id : "5"
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

		$scope.search.area = "ALL";
		$scope.search.amount = "ALL";
		$scope.search.isShore = "ALL";

		$scope.filterdCountries = $scope.countries;

		$scope.countryClick = function(country) {
			$scope.model.map.center = {
				lat : country.center.latitude,
				lng : country.center.longitude
			}

			$scope.selectedCountry = country.name;
			$scope.map.setCenter($scope.model.map.center);
			$scope.map.setZoom(country.zoom);
			$scope.marker.setPosition($scope.model.map.center);
		};

		$scope.filterCountries = function(country) {
			if ($scope.search.area == "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore == "ALL") {
				return true;
			} else if ($scope.search.area != "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore != "ALL") {
				if (country.area == $scope.search.area && country.price <= $scope.search.amount
						&& country.type == $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore == "ALL") {
				if (country.area == $scope.search.area) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore != "ALL") {
				if (country.type == $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore == "ALL") {
				if (country.price <= $scope.search.amount) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore == "ALL") {
				if (country.area == $scope.search.area && country.price <= $scope.search.amount) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore != "ALL") {
				if (country.area == $scope.search.area && country.type <= $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore != "ALL") {
				if (country.type == $scope.search.isShore && country.price <= $scope.search.amount) {
					return true;
				}
			}

			return false;
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