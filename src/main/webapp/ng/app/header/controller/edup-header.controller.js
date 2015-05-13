'use strict';

angular.module('edup.header')

    .controller('HeaderController', function ($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.downloadDocument = function () {
            window.open('https://172.20.10.4:8443/edup/api/documents');
        };
    }
);