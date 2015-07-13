'use strict';

angular.module('edup.subjects')

    .directive('eventsHeader', function ($timeout) {
        return {
            restrict: 'E',
            templateUrl: 'events-header',
            controller: function ($scope, $timeout, QueryService, RestService) {

                $scope.subjects = [];
                $scope.selectedSubject = {};

                var initTypeAhead = function (values) {
                    var typeAhead = new Bloodhound({
                        datumTokenizer: Bloodhound.tokenizers.whitespace,
                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                        local: values
                    });

                    $('#bloodhound .typeahead').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 1
                        },
                        {
                            name: 'subjectsTypeAhead',
                            source: typeAhead
                        }
                    );
                };

                function searchSubjects(query) {
                    RestService.Private.Subjects.get(query).then(function (response) {
                        $scope.subjects = response.values;
                        initTypeAhead(_.pluck($scope.subjects, 'subjectName'))
                    });
                }

                searchSubjects(QueryService.Query(999, 0, '*', 'Created desc', null, true), '*');


                $scope.selectSubject = function (selectedSubjectName) {

                    $scope.selectedSubject = _.find($scope.subjects, function (subject) {
                        return subject.subjectName === selectedSubjectName;
                    });

                };

            },
            link: function (scope) {

            }
        };
    }
);