'use strict';

angular.module('edup.common')

    .directive('windowSize', function ($document, $window) {
        return {
            restrict: 'A',
            link: function (scope) {
                var windowHeight = $window.innerHeight || $document.body.clientHeight;
                var headerHeight = $('#edupHeader').height();
                scope.viewScrollPaneHeight = windowHeight - headerHeight - 40;
                console.log(scope.viewScrollPaneHeight);
            }
        };
    }
);
