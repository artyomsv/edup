'use strict';

angular.module('edup.navbar')

    .controller('NavbarController', function ($scope, $state) {
        $scope.menuModel = {
            items: ['Clients', 'Calendar'],
            states: ['clients', 'calendar'],
            current: 0
        };

        $scope.$watch(function () {
            return $scope.menuModel.current;
        }, function (index) {
            $state.go($scope.menuModel.states[index]);
        });
    }
);