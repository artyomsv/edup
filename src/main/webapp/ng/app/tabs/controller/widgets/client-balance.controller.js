'use strict';

angular.module('edup.tabs')

    .controller('ClientBalanceController', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = Math.floor((Math.random() * 100) - 50);
    }
);

