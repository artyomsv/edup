'use strict';

angular.module('edup.tabs')

    .directive('clientsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'clients-list',
            link : function ($scope) {
                $scope.directiveTest = 'Clients list';
            }
        };
    }
);