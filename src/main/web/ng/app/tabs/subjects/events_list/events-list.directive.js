'use strict';

angular.module('edup.subjects')

	.directive('eventsList', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-list',
			controller: function ($scope, $timeout, QueryService, RestService) {

				$scope.events = {
					values: [],
					total: 0,
					loading: false,
					firstLoad : true
				};

				$scope.setSelected = function (event) {
					$scope.events.firstLoad = false;
					$scope.loadEventDetails(event.eventId);
				};

				$scope.loadMoreEvens = function (force) {
					if (!force && $scope.events.values.length !== 0 && ($scope.events.values.length === $scope.events.total)) {
						return;
					}

					if (force) {
						$scope.events = {
							values: [],
							total: 0,
							loading: false
						};
					}

					if (!$scope.events.loading) {
						$scope.events.loading = true;

						//var query = QueryService.Query(10, $scope.events.values.length, '*', 'Created desc', null, true);
						var query = QueryService.Query(13, $scope.events.values.length, '*', 'EventDate desc,From desc', null, true);

						$timeout(function () {
							RestService.Private.Subjects.one('events').get(query).then(function (response) {
								var events = response.values;
								if ($scope.events.firstLoad && events.length !== 0) {
									$scope.setSelected(events[0]);
								}
								_.forEach(events, function (event) {
									event.priceAdjusted = event.price / 100;
									$scope.events.values.push(event);
								});

								$scope.events.total = response.count;
								$scope.events.loading = false;
							});
						}, 300);
					}
				};

			},
			link: function (scope) {

			}
		};
	}
);