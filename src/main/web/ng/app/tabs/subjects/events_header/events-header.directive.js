'use strict';

angular.module('edup.subjects')

	.directive('eventsHeader', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-header',
			controller: function ($scope, $timeout, $filter, moment, QueryService, RestService, TypeAheadService, NotificationService) {

				$scope.subjects = [];
				$scope.selectedSubject = null;


				var selectSubject = function (selectedSubjectName) {
					$scope.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});
					if (!!$scope.selectedSubject) {
						$scope.subjectEvent.subjectName = $scope.selectedSubject.subjectName;
					}
				};

				var typeAhead = TypeAheadService.Build();

				var $bloodhound = $('#bloodhound .typeahead');

				$bloodhound.typeahead({
						hint: true,
						highlight: true,
						minLength: 1
					},
					{
						name: 'subjectsTypeAhead',
						source: typeAhead
					}
				);

				$bloodhound.bind('typeahead:selected', function (obj, datum) {
					selectSubject(datum);
				});

				var isSubjectDataFilled = function (newSubjectEvent) {
					if (!newSubjectEvent) {
						return false;
					}
					if (!newSubjectEvent.amount) {
						return false;
					}
					if (!newSubjectEvent.eventDate) {
						return false;
					}
					if (!newSubjectEvent.eventTimeFrom) {
						return false;
					}
					if (!newSubjectEvent.eventTimeTo) {
						return false;
					}
					return true;
				};

				var reset = function () {
					$bloodhound.typeahead('val', '');
					$scope.subjectEvent = null;
					$scope.selectedSubject = null;
				};

				var calculateTime = function (date, time) {
					return $filter('date')(date, 'yyyy-MM-dd') + ' ' + $filter('date')(time, 'HH:mm')
				};

				$scope.renderDatePicker = function ($view, $dates, $leftDate, $upDate, $rightDate) {
					_.forEach($dates, function (date) {
						if (moment(date.utcDateValue).isBefore(moment())) {
							date.selectable = false;
						}
					})
				};

				$scope.renderTimePicker = function ($view, $dates, $leftDate, $upDate, $rightDate) {
					$leftDate.selectable = false;
					$upDate.selectable = false;
					$rightDate.selectable = false;
				};

				$scope.saveNewEvent = function (newSubjectEvent) {
					if (isSubjectDataFilled(newSubjectEvent)) {

						var shouldSave = false;

						var payload = {};
						payload.eventDate = newSubjectEvent.eventDate;
						payload.from = new Date(calculateTime(newSubjectEvent.eventDate, newSubjectEvent.eventTimeFrom));
						payload.to = new Date(calculateTime(newSubjectEvent.eventDate, newSubjectEvent.eventTimeTo));
						payload.price = newSubjectEvent.amount * 100;
						payload.subject = {};
						if (!!$scope.selectedSubject) {
							payload.subject.subjectName = $scope.selectedSubject.subjectName;
							payload.subject.subjectId = $scope.selectedSubject.subjectId;
							shouldSave = true;
						} else if (!_.isEmpty(newSubjectEvent.subjectName)) {
							payload.subject.subjectName = newSubjectEvent.subjectName;
							shouldSave = true;
						}

						if (shouldSave) {
							RestService.Private.Subjects.one('events').customPOST(payload).then(function () {
								reset();
								if (payload.subject.subjectId) {
									NotificationService.Success('Event have been registered to subject \"' + payload.subject.subjectName + '\"');
								} else {
									NotificationService.Success('New subject have been created.\n Event have been registered to subject \"' + payload.subject.subjectName + '\"');
								}

								$scope.loadMoreEvens(true);

							});
						} else {
							NotificationService.Error(angular.toJson(newSubjectEvent, true));
						}
					}
				};

				$scope.updateSelectedSubject = function () {
					var inputValue = $bloodhound.typeahead('val');
					if ($scope.selectedSubject) {
						if ($scope.selectedSubject.subjectName === inputValue) {
							console.log('value same in input and typeahead');
							$scope.subjectEvent.subjectName = inputValue;
						} else {
							console.log('value not same in input and typeahead');
							$scope.selectedSubject = null;
							$scope.subjectEvent.subjectName = inputValue;
						}
					} else {
						console.log('typeahead not selected');
						$scope.subjectEvent.subjectName = inputValue;
					}
				};

			},
			link: function (scope) {

			}
		};
	}
);