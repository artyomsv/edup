'use strict';

angular.module('edup.widgets')

    .directive('newStudentRecord', function () {
        return {
            restrict: 'E',
            scope: {
                loadStudents: '&',
                paging: '='
            },
            templateUrl: 'new-student-modal',
            link: function (scope) {


            }
        };
    }
);