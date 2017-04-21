(function() {

	'use strict';

	/* directives Module */

	var module = angular.module('directives', []);

	module.directive('header', function() {
		return {
			restrict : 'A',
			link : function(scope, elemnet, attrs) {
				$(window).on("scroll", function() {
					if ($(this).scrollTop() > 40) {
						$("nav").addClass("nav-height-small");
					} else if ($(this).scrollTop() < 40) {
						$("nav").removeClass("nav-height-small");
					}
				});
			}
		}
	});

})();