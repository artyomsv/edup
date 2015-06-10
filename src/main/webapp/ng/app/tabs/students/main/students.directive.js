'use strict';

angular.module('edup.students')

    .directive('students', function () {
        return {
            restrict: 'E',
            templateUrl: 'students',
            link : function ($scope) {
                $scope.directiveTest = 'Students tab directive';
            }
        };
    }
);