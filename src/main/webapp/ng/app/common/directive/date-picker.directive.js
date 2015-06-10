'use strict';

angular.module('edup.common')

    .directive('datePicker', function ($timeout, $filter) {
        return {
            restrict: 'E',
            templateUrl: 'date-picker',
            scope: {
                inputField: '=',
                label: '@',
                datePickerId: '@'
            },
            controller: function ($scope) {

            },

            link: function (scope) {
                $timeout(function () {
                    var datePicker = $('#' + scope.datePickerId);

                    datePicker.datetimepicker({
                        viewMode: 'years',
                        format: 'YYYY-MM-DD'
                    });

                    datePicker.on('dp.change', function (event) {
                        scope.inputField = $filter('date')(new Date(event.date), 'yyyy-MM-dd')

                    });
                });
            }
        };
    }
);
