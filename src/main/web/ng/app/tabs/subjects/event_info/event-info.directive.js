'use strict';

angular.module('edup.subjects')

	.directive('eventInfo', function () {
		return {
			restrict: 'E',
			templateUrl: 'event-info',
			controller: function ($scope, RestService, QueryService) {

				$scope.studentsManagemnt = {
					expanded: false
				};

				$scope.selectedEvent = {
					loaded: false
				};

				$scope.loadEventDetails = function (eventId) {
					if (eventId) {
						$scope.resetEventStudentsSearch();

						RestService.Private.Subjects.one('events').one(eventId.toString()).get().then(function (response) {
							if (response.payload) {
								$scope.selectedEvent = response.payload;
								$scope.selectedEvent.loaded = true;
								$scope.selectedEvent.adjustedPrice = $scope.selectedEvent.price / 100;

								$scope.loadAttendance(eventId);
							}
						});
					}
				};

				$scope.loadAttendance = function (eventId) {
					if (eventId) {
						var query = QueryService.Query(999, 0, '*', null, 'EventId eq ' + eventId, false);
						RestService.Private.Subjects
							.one('events')
							.one('attendance')
							.get(query)
							.then(function (response) {
								$scope.eventStudentsSearch.attendance = response.values;
								if ($scope.studentsManagemnt.expanded) {
									$scope.executeSearch();
								}
							});
					}
				};

				$scope.processExpand = function () {
					$scope.studentsManagemnt.expanded = !$scope.studentsManagemnt.expanded;
					if ($scope.studentsManagemnt.expanded) {
						$scope.executeSearch();
					}
				};

			},
			link: function (scope) {

			}
		};
	}
);