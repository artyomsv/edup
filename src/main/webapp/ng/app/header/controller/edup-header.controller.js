'use strict';

angular.module('edup.header')

    .controller('HeaderController', function ($scope, $location, Restangular) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
    }
);