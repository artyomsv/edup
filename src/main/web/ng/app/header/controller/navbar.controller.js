'use strict';

angular.module('edup.header')

    .controller('NavbarController', function ($scope, $state) {
        $scope.appModel = {
            items: ['Students', 'Subjects', 'Calendar'],
            states: ['students', 'subjects', 'calendar'],
            current: 0
        };

        $scope.$watch(function () {
            return $scope.appModel.current;
        }, function (index) {
            $state.go($scope.appModel.states[index]);
        });

    }
);