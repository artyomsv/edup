'use strict';

angular.module('edup.header')

    .controller('NavbarController', function ($scope, $state) {
        $scope.calendarModel = {
            items: ['Students', 'Calendar'],
            states: ['students', 'calendar'],
            current: 0
        };

        $scope.$watch(function () {
            return $scope.calendarModel.current;
        }, function (index) {
            $state.go($scope.calendarModel.states[index]);
        });

    }
);