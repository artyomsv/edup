'use strict';

angular.module('edup.widgets')

    .directive('identificationCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'identification-card',
            link : function ($scope) {
            }
        };
    }
);