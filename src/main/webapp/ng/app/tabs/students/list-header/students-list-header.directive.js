'use strict';

angular.module('edup.students')

    .directive('studentsListHeader', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list-header',
            link : function (scope) {

            }
        };
    }
);