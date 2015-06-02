'use strict';

angular.module('edup.students')

    .directive('studentAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-attendance',
            link : function ($scope) {
            }
        };
    }
);