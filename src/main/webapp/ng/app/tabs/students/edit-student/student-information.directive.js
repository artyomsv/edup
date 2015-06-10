'use strict';

angular.module('edup.students')

    .directive('studentInformation', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-information',
            link : function ($scope) {

            }
        };
    }
);