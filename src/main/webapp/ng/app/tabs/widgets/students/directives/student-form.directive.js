'use strict';

angular.module('edup.widgets')

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