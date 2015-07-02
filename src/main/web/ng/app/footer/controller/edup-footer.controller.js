'use strict';

angular.module('edup.footer')

    .controller('FooterController', function ($scope, $location, Restangular) {
        Restangular.one('ping').get().then(
            function (result) {
                $scope.appVersion = result.version;
            }
        );
    }
);