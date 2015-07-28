'use strict';

angular.module('edup.subjects')

	.directive('eventInfo', function () {
		return {
			restrict: 'E',
			templateUrl: 'event-info',
			controller: function ($scope, moment, $filter, RestService, QueryService) {

				$scope.studentsManagemnt = {
					expanded: false,
					confirmation: false
				};

				$scope.selectedEvent = {
					loaded: false
				};

				$scope.loadEventDetails = function (event) {
					if (event && event.eventId) {

						RestService.Private.Subjects.one('events').one(event.eventId.toString()).get().then(function (response) {
							if (response.payload) {
								$scope.selectedEvent = response.payload;
								$scope.selectedEvent.loaded = true;
								$scope.selectedEvent.adjustedPrice = $scope.selectedEvent.price / 100;
								$scope.selectedEvent.havePassed = moment().isAfter($scope.selectedEvent.eventDate);
								$scope.selectedEvent.currentStatus = event.currentStatus;

								$scope.resetEventStudentsSearch($scope.selectedEvent.havePassed);

								$scope.loadAttendance(event.eventId);
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
								$scope.executeSearch();
							});
					}
				};

				$scope.processExpand = function () {
					$scope.studentsManagemnt.expanded = !$scope.studentsManagemnt.expanded;
					$scope.executeSearch();
				};


			},
			link: function (scope) {

			}
		};
	}
);