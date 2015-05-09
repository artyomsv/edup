'use strict';

angular.module('edup.tabs')

    .directive('clientBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-balance',
            link : function ($scope) {
                $scope.directiveTest = 'Client balance tabbed pane directive';

                $scope.openDatePicker = function() {
                    $('#datetimepicker').datetimepicker();
                };
            }
        };
    }
);