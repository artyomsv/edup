'use strict';

angular.module('edup.header')

    .directive('edupHeader', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-header',

            controller: function ($scope, $window, $location, RestService, UrlService) {

                $scope.isActive = function (viewLocation) {
                    return viewLocation === $location.path();
                };

                $scope.downloadReport = function () {
                    window.open('https://172.20.10.4:8443/edup/api/private/reports');
                };

                $scope.logoutUser = function () {
                    RestService.Private.LogOut.post().then(function () {
                        $window.location.href = UrlService.BaseUrl;
                    });
                };
            },

            link: function () {

            }

        };
    }
);