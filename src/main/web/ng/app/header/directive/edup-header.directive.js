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