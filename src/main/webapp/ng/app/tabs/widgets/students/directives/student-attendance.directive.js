'use strict';

angular.module('edup.widgets')

    .directive('studentAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-attendance',
            link : function ($scope) {
            }
        };
    }
);