'use strict';

angular.module('edup.tabs')

    .directive('tabsClient', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-client',
            link : function ($scope) {
                $scope.directiveTest = 'Client tab directive';
            }
        };
    }
);