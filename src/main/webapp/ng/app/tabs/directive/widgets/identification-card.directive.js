'use strict';

angular.module('edup.tabs')

    .directive('identificationCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'identification-card',
            link : function ($scope) {
            }
        };
    }
);