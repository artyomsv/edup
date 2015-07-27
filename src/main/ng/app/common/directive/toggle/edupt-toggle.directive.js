'use strict';

angular.module('edup.common')

    .directive('edupToggle', function ($timeout) {
        return {
            restrict: 'E',
            templateUrl: 'edup-toggle',
            scope: {
                onEvent: '&',
                offEvent: '&',
                onLabel: '@',
                offLabel: '@',
                toggleId: '@',
                linkWidget: '='
            },
            controller: function ($scope) {

            },

            link: function (scope) {
                $timeout(function () {
                    if (!scope.onLabel) {
                        scope.onLabel = 'On';
                    }
                    if (!scope.offLabel) {
                        scope.offLabel = 'Off';
                    }

                    var toggle = $('#' + scope.toggleId);

                    toggle.bootstrapToggle();

                    toggle.change(function () {
                        if ($(this).prop('checked')) {
                            scope.onEvent();
                        } else {
                            scope.offEvent();
                        }
                    });

                });
            }
        };
    }
);
