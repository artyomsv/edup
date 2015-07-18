'use strict';

angular.module('edup.subjects')

	.directive('eventsHeader', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-header',
			controller: function ($scope, $timeout, moment, QueryService, RestService, TypeAheadService) {

				$scope.subjects = [];
				$scope.selectedSubject = {};
				$scope.subjectSelected = false;


				var selectSubject = function (selectedSubjectName) {
					$scope.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});

					$scope.subjectSelected = !!$scope.selectedSubject;
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
					$scope.subjectEvent = {};
					$scope.subjectSelected = null;
					$scope.selectedSubject = false;
				};

				$scope.saveNewEvent = function (newSubjectEvent) {
					if (isSubjectDataFilled(newSubjectEvent)) {

						var shouldSave = false;

						var payload = {};
						payload.eventDate = new Date(newSubjectEvent.eventDate);
						payload.from = new Date(newSubjectEvent.eventDate + ' ' + newSubjectEvent.eventTimeFrom);
						payload.to = new Date(newSubjectEvent.eventDate + ' ' + newSubjectEvent.eventTimeTo);
						payload.price = newSubjectEvent.amount * 100;
						payload.subject = {};
						if ($scope.subjectSelected) {
							payload.subject.subjectName = $scope.selectedSubject.subjectName;
							payload.subject.subjectId = $scope.selectedSubject.subjectId;
							shouldSave = true;
						} else if (!_.isEmpty(newSubjectEvent.subjectName)) {
							payload.subject.subjectName = newSubjectEvent.subjectName;
							shouldSave = true;
						}

						if (shouldSave) {
							RestService.Private.Subjects.one('events').customPOST(payload).then(function (response) {
								reset();
								console.log(angular.toJson('Event ID: ' + response.payload));
							});
						}
					}
				};

			},
			link: function (scope) {

			}
		};
	}
);