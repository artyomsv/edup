'use strict';

angular.module('edup.students')

    .directive('studentInformation', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-information',
            link : function ($scope) {
                $scope.directiveTest = 'Student Form tabbed pane directive';
            }
        };
    }
);