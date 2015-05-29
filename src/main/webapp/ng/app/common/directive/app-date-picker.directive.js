'use strict';

angular.module('edup.common')

    .directive('appDatePicker', function () {
        return {
            restrict: 'A',
            scope: {
                selectedDate: '=pickerValue'
            },
            link: function (scope, element) {
                element.datetimepicker();

                element.on('dp.change dp.hide', function (e) {
                    scope.selectedDate = e.date;
                });

            }
        };
    }
);
