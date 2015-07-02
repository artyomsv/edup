'use strict';

angular.module('edup.login')

    .directive('edupLogin', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-login',

            controller: function ($scope, $window, RestService, UrlService, NotificationService) {
                $scope.submitLogin = function (user, password) {
                    RestService.Public.Login.customPOST(
                        'j_username=' + user + '&j_password=' + password,
                        undefined,
                        {},
                        {'Content-Type': 'application/x-www-form-urlencoded'})

                        .then(function () {
                            $window.location.href = UrlService.BaseUrl;
                        });
                };
            },

            link: function () {

            }
        };
    }
);
