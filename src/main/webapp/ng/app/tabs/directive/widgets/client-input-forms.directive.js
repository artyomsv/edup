'use strict';

angular.module('edup.tabs')

    .directive('clientInputForms', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-input-forms',
            link : function ($scope) {
                $scope.directiveTest = 'Client input forms';
            }
        };
    }
);