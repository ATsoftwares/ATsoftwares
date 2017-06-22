/**
 * directives
 */
app.directive('dateInit', function() {
	return {
		restrict : 'A',
		link : function($scope, element, attrs) {
			$('.datepickerInput').datepicker({
				format : 'dd/mm/yyyy',
				orientation : 'bottom auto'
			});
		}
	}
}).directive('formattedDate', function(dateFilter) {
	return {
		require : 'ngModel',
		scope : {
			format : "@"
		},
		link : function(scope, element, attrs, ngModelController) {
			ngModelController.$parsers.push(function(data) {
				// convert data from view format to model format
				if (data.length < 10) {
					return data;
				}
				var x = new Date(data);
				return dateFilter(data, scope.format); // converted
			});

			ngModelController.$formatters.push(function(data) {
				// convert data from model format to view format
				return dateFilter(data, scope.format); // converted
			});
		}
	}
});