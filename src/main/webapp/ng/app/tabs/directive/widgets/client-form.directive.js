'use strict';

angular.module('edup.tabs')

    .directive('clientForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-form',
            link : function ($scope) {
                $scope.directiveTest = 'Client Form tabbed pane directive';
                console.log(angular.toJson($scope.selectedClient, true))
            }
        };
    }
);