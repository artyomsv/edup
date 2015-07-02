'use strict';

angular.module('edup.calendar')

    .directive('сalendar', function () {
        return {
            restrict: 'E',
            templateUrl: 'calendar',
            link: function ($scope) {
            }
        };
    }
);