'use strict';

angular.module('edup.navbar')

    .controller('NavbarController', function ($scope, $state) {
        $scope.calendarModel = {
            items: ['Clients', 'Calendar'],
            states: ['clients', 'calendar'],
            current: 0
        };

        $scope.$watch(function () {
            return $scope.calendarModel.current;
        }, function (index) {
            $state.go($scope.calendarModel.states[index]);
        });

    }
);