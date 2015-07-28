'use strict';

angular.module('edup.subjects')

	.directive('confirmEventFinishedModal', function () {
		return {
			restrict: 'E',
			templateUrl: 'confirm-event-finished-modal',
			controller: function ($scope, $timeout, moment, RestService) {

				$scope.confirmEventIsFinished = function () {
					if ($scope.studentsManagemnt.confirmation) {
						return;
					}

					$scope.studentsManagemnt.confirmation = true;

					var payload = _.cloneDeep($scope.selectedEvent);
					payload.status = 'FINALIZED';

					RestService.Private.Subjects
						.one('events')
						.one(payload.eventId.toString())
						.customPUT(payload)
						.then(function (response) {
							$scope.selectedEvent.status = payload.status;
							$scope.selectedEvent.currentStatus = 'CONFIRMED';
							var event = _.find($scope.events.values, function (event) {
								return event.eventId === payload.eventId;
							});

							if (event) {
								event.status = $scope.selectedEvent.status;
								event.currentStatus = $scope.selectedEvent.currentStatus;
							}

							$scope.dismissModal();
							$scope.studentsManagemnt.confirmation = false;
						}, function (error) {
							$scope.studentsManagemnt.confirmation = false;
						});

				};


			},
			link: function (scope) {

			}
		};


	}
);