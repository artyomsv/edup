'use strict';

angular.module('edup.tabs')

    .directive('tabsStudents', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-students',
            link : function ($scope) {
                $scope.directiveTest = 'Students tab directive';
            }
        };
    }
);