'use strict';

angular.module('edup.client')

    .directive('clientAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-attendance',
            link : function ($scope) {
                $scope.directiveTest = 'Client attendance tabbed pane directive';
            }
        };
    }
);