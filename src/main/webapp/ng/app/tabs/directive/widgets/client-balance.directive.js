'use strict';

angular.module('edup.tabs')

    .directive('clientBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-balance',
            link : function ($scope) {
                $scope.newBalanceValue = '';
                
                $scope.openDatePicker = function() {
                    $('#datetimepicker').datetimepicker();
                };
            }
        };
    }
);