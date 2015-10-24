'use strict';

angular.module('edup.reports')

	.directive('eventAttendanceModal', function ($filter, UrlService) {
		return {
			restrict: 'E',
			templateUrl: 'event-attendance-modal',
			controller: function ($scope, $timeout, RestService, TypeAheadService) {

				$scope.plannedEventDetails = {
					selectedSubject: null,
					dateFromPicker: null,
					dateToPicker: null
				};

				var selectSubject = function (selectedSubjectName) {
					$scope.plannedEventDetails.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});
					console.log(angular.toJson($scope.selectedSubject));
				};


				var typeAhead = TypeAheadService.Build();

				var $bloodhound = $('#subject-event-typeahead-modal .typeahead');

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

				$scope.plannedEventJournal = function () {
					$scope.plannedEventDetails = {
						selectedSubject: null,
						dateFromPicker: null,
						dateToPicker: null,
						subjectName: null,
						showAttendance: false
					};
				};

				$scope.finishedEventReport = function () {
					$scope.plannedEventDetails = {
						selectedSubject: null,
						dateFromPicker: null,
						dateToPicker: null,
						subjectName: null,
						showAttendance: true
					};
				};


			},
			link: function (scope) {

				scope.renderDatePickerForReport = function ($view, $dates, $leftDate, $upDate, $rightDate) {

				};

				scope.performReportDownload = function (details) {
					if (details && details.selectedSubject && details.dateFromPicker && details.dateToPicker) {
						var query = {
							from: $filter('date')(details.dateFromPicker, 'ddMMyyyy'),
							to: $filter('date')(details.dateToPicker, 'ddMMyyyy'),
							attendance: scope.plannedEventDetails.showAttendance
						};
						var url = UrlService.Reports.Events + '/' + details.selectedSubject.subjectId + '?from=' + query.from + '&to=' + query.to + '&attendance=' + query.attendance;
						window.open(url);
						scope.dismissModal();
					}

				};

			}
		};


	}
);