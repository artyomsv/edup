'use strict';

angular.module('edup.header')

    .controller('HeaderController', function ($scope, Restangular) {
        $scope.appName = 'unknown';
        $scope.appVersion = 'unknown';
        Restangular.one('ping').get().then(
            function (result) {
                $scope.appName = result.app;
                $scope.appVersion = result.version;
            }
        );
    }
);