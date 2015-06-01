'use strict';

angular.module('edup.common')

    .directive('appModal', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissModal = function () {
                    element.modal('hide');
                };
            }
        };
    }
);
