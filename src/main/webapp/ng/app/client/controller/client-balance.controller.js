'use strict';

angular.module('edup.client')

    .controller('ClientBalanceController', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = Math.floor((Math.random() * 100) - 50);
    }
);

