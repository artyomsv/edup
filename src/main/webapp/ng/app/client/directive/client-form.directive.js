'use strict';

angular.module('edup.client')

    .directive('clientForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-form',
            link : function ($scope) {
                $scope.directiveTest = 'Client Form tabbed pane directive';
            }
        };
    }
);