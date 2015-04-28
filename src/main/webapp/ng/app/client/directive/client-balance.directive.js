'use strict';

angular.module('edup.client')

    .directive('clientBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-balance',
            link : function ($scope) {
                $scope.directiveTest = 'Client balance tabbed pane directive';
            }
        };
    }
);