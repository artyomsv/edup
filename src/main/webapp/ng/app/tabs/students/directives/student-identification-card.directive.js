'use strict';

angular.module('edup.students')

    .directive('studentIdentificationCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-identification-card',
            link : function ($scope) {
            }
        };
    }
);