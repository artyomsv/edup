'use strict';

angular.module('edup.tabs')

    .controller('StudentBalanceController', function ($scope) {
        $scope.addToBalance = function (value) {
            $scope.selectedStudent.balance = $scope.selectedStudent.balance + parseInt(value);
        };

    }
);

