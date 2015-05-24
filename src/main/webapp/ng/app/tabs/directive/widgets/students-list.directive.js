'use strict';

angular.module('edup.tabs')

    .directive('studentsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list',
            link : function ($scope) {
                $scope.directiveTest = 'Students list';
            }
        };
    }
);