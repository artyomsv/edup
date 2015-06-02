'use strict';

angular.module('edup.widgets')

    .directive('studentNewRecord', function () {
        return {
            restrict: 'E',
            templateUrl: 'new-student-modal',
            link: function (scope) {
            }
        };
    }
);