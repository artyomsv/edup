'use strict';

angular.module('edup.login')

    .directive('edupLogin', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-login',
            link: function (scope) {
                scope.test = 'test';
            }
        };
    }
);
