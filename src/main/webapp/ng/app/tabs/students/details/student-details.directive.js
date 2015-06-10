'use strict';

angular.module('edup.students')

    .directive('studentDetails', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-details',
            link : function ($scope) {
                $scope.directiveTest = 'Student input forms';
            }
        };
    }
);