/**
 * services *
 */
app.factory('$demoHttp', [ '$rootScope', '$http', '$state', function($rootScope, $http, $state) {
	return {
		get : function(url, data, callback) {
			var getUrl = "/app" + url;
			$http({
				method : 'GET',
				data : '',
				url : getUrl,
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(data) {
				callback(data);
			}).error(function(data) {
				console.log('error');
			});
		},
		post : function(url, data, headers, callback) {
			var postUl = '/app' + url;
			var headers = headers == null ? {
				'Content-Type' : 'application/json',
				'Accept' : 'application/json'
			} : headers;
			$http({
				method : 'POST',
				data : encodeURIComponent(angular.toJson(data)),
				url : postUl,
				headers : headers
			}).success(function(data) {
				callback(data);
			}).error(function(data) {
				console.log('error');
			});
		}
	}
} ]).factory('$idleTimeout',
		[ '$timeout', '$rootScope', '$state', '$cookies', function($timeout, $rootScope, $state, $cookies) {

			var timer = null;
			var logoffMinutes = 100;

			var activeMethod = function(awayTimeout) {
				console.log('activeMethod');
				if (timer != null) {
					$timeout.cancel(timer);
					timer = null;
				}
				var logoff = function() {
					$cookies.remove("loggedInUser", {
						path : '/'
					});
					idleTimeout.stop();
					alert('user is unauthorized');
					$state.go('login');
				}

				timer = $timeout(logoff, awayTimeout * 60 * 1000, false);
			};

			function throttle(callback, limit) {
				var wait = false;
				return function() {
					if (!wait) {
						callback.call();
						wait = true;
						setTimeout(function() {
							wait = false;
						}, limit);
					}
				}
			}
			;

			var listener = throttle(function() {
				activeMethod(logoffMinutes);
			}, 5000);

			var idleTimeout = {
				'start' : function() {
					window.addEventListener('click', listener);
					window.addEventListener('mousemove', listener);
					window.addEventListener('mouseenter', listener);
					window.addEventListener('keydown', listener);
					window.addEventListener('scroll', listener);
					window.addEventListener('mousewheel', listener);
				},
				'stop' : function() {
					if (timer != null) {
						$timeout.cancel(timer);
						$cookies.remove("loggedInUser", {
							path : '/'
						});
						timer = null;
						window.removeEventListener('click', listener);
						window.removeEventListener('mousemove', listener);
						window.removeEventListener('mouseenter', listener);
						window.removeEventListener('keydown', listener);
						window.removeEventListener('scroll', listener);
						window.removeEventListener('mousewheel', listener);
					}
				}
			}

			return idleTimeout;

		} ])

.factory('XSRFInterceptor', [ '$cookies', '$log', function($cookies, $log) {

	var XSRFInterceptor = {

		request : function(config) {

			var token = $cookies.get('XSRF-TOKEN');

			if (token) {
				config.headers['X-XSRF-TOKEN'] = token;
				/* $log.info("X-XSRF-TOKEN: " + token); */
			}

			return config;
		}
	};
	return XSRFInterceptor;
} ]);