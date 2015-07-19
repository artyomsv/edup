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
					loading: false
				};

				$scope.setSelected = function (event) {
					console.log('Selected event ID: ' + event.eventId);
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
						var query = QueryService.Query(10, $scope.events.values.length, '*', 'EventDate desc,From desc', null, true);

						$timeout(function () {
							RestService.Private.Subjects.one('events').get(query).then(function (response) {
								var events = response.values;
								console.log('Loaded: ' + events.length);
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