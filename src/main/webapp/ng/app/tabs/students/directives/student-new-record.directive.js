'use strict';

angular.module('edup.students')

    .directive('studentNewRecord', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-new-record',
            link: function (scope) {
                scope.birth = 'birth';
                scope.birthDate = 'date';
            }
        };
    }
);