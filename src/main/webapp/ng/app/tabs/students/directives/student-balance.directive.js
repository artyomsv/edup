'use strict';

angular.module('edup.students')

    .directive('studentBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-balance',
            link : function ($scope) {
                $scope.newBalanceValue = '';
                
                $scope.openDatePicker = function() {
                    $('#datetimepicker').datetimepicker();
                };
            }
        };
    }
);