'use strict';

angular.module('edup.widgets')

    .directive('newStudentRecord', function () {
        return {
            restrict: 'E',
            templateUrl: 'new-student-modal',
            link: function (scope) {
            }
        };
    }
);