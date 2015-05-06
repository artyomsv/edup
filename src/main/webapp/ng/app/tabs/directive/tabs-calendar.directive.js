'use strict';

angular.module('edup.tabs')

    .directive('tabsCalendar', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-calendar',
            link : function ($scope) {
                $scope.directiveTest = 'Calendar tab directive';
            }
        };
    }
);