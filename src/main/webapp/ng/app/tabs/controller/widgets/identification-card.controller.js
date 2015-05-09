'use strict';

angular.module('edup.tabs')

    .controller('IdentificationCardController', function ($scope) {

        $scope.addToBalance = function (value) {
            $scope.selectedClient.balance = $scope.selectedClient.balance + parseInt(value);
        };

    }
);

