'use strict';

angular.module('edup.header')

    .controller('HeaderController', function ($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
    }
);