'use strict';

angular.module('edup.tabs')

    .directive('studentForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-form',
            link : function ($scope) {
                $scope.directiveTest = 'Student Form tabbed pane directive';
            }
        };
    }
);