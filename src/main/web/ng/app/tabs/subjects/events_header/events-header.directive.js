'use strict';

angular.module('edup.subjects')

	.directive('eventsHeader', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-header',
			controller: function ($scope, $timeout, QueryService, RestService) {

				$scope.subjects = [];
				$scope.selectedSubject = {};
				$scope.subjectSelected = false;


				var selectSubject = function (selectedSubjectName) {
					$scope.selectedSubject = _.find($scope.subjects, function (subject) {
						return subject.subjectName === selectedSubjectName;
					});

					$scope.subjectSelected = !!$scope.selectedSubject;
				};

				var initTypeAhead = function (values) {
					var typeAhead = new Bloodhound({
						datumTokenizer: Bloodhound.tokenizers.whitespace,
						queryTokenizer: Bloodhound.tokenizers.whitespace,
						local: values
					});

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
				};

				function searchSubjects(query) {
					RestService.Private.Subjects.get(query).then(function (response) {
						$scope.subjects = response.values;
						initTypeAhead(_.pluck($scope.subjects, 'subjectName'));
					});
				}

				searchSubjects(QueryService.Query(999, 0, '*', 'Created desc', null, true));

				$scope.updateSelectedSubject = function () {
					$scope.subjectSelected = !!$scope.selectedSubject;
				};

				$scope.saveNewEvent = function (newSubjectEvent) {
					console.log(angular.toJson(newSubjectEvent, true));
				};

			},
			link: function (scope) {

			}
		};
	}
);