'use strict';

angular.module('edup.tabs')

    .directive('clientAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-attendance',
            link : function ($scope) {
            }
        };
    }
);