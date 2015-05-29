'use strict';

angular.module('edup.students')

    .directive('studentInputForms', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-input-forms',
            link : function ($scope) {
                $scope.directiveTest = 'Student input forms';
            }
        };
    }
);