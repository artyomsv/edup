'use strict';

angular.module('edup.students')

    .directive('studentsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list',
            link : function ($scope) {
            }
        };
    }
);