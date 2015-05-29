'use strict';

angular.module('edup.common')

    .directive('appModal', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismiss = function () {
                    element.modal('hide');
                };
            }
        };
    }
);
