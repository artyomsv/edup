'use strict';

angular.module('edup.widgets')

    .directive('newStudent', function () {
        return {
            restrict: 'E',
            templateUrl: 'new-student',
            link: function (scope) {
                scope.birth = 'birth';
                scope.birthDate = 'date';
            }
        };
    }
);