'use strict';

angular.module('edup.subjects')

	.directive('eventsList', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-list',
			controller: function ($scope, QueryService, RestService) {

				$scope.events = [];

				$scope.setSelected = function (event) {
					console.log('Selected event ID: ' + event.eventId);
				};

				$scope.loadMoreEvens = function () {
					var query = QueryService.Query(10, 0, '*', 'Created desc', null, true);
					RestService.Private.Subjects.one('events').get(query).then(function (response) {
						var events = response.values;
						_.forEach(events, function (event) {
							$scope.events.push(event);
						});
					});
				};

				$scope.loadMoreEvens();


			},
			link: function (scope) {

			}
		};
	}
);