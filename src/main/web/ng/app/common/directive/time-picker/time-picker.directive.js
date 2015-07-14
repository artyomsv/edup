'use strict';

angular.module('edup.common')

	.directive('timePicker', function ($timeout, $filter) {
		return {
			restrict: 'E',
			templateUrl: 'time-picker',
			scope: {
				inputField: '=',
				label: '@',
				pickerPlaceholder: '@',
				timePickerId: '@'
			},
			controller: function ($scope) {

			},

			link: function (scope) {
				$timeout(function () {
					var timePicker = $('#' + scope.timePickerId);

					timePicker.datetimepicker({
						//viewMode: 'years',
						format: 'HH:mm'
					});

					timePicker.on('dp.change', function (event) {
						scope.inputField = $filter('date')(new Date(event.date), 'HH:mm');
					});


				});
			}
		};
	}
);
