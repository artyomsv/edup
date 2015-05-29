'use strict';

angular.module('edup.widgets')

    .directive('studentDocuments', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-documents',
            link : function ($scope) {
                $scope.directiveTest = 'Student tabbed pane directive';
            }
        };
    }
);