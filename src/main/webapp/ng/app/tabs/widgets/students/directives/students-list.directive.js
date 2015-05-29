'use strict';

angular.module('edup.widgets')

    .directive('studentsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list',
            link : function ($scope) {
            }
        };
    }
);