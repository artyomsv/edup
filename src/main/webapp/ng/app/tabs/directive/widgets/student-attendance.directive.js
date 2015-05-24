'use strict';

angular.module('edup.tabs')

    .directive('studentAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-attendance',
            link : function ($scope) {
            }
        };
    }
);