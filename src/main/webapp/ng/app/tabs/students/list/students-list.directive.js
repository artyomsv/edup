'use strict';

angular.module('edup.students')

    .directive('studentsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list',
            link: function (scope) {

                scope.openStudentDetailModal = function (student) {
                    $('#student-details-modal-view').modal('show');
                    $('.nav-tabs a[href="#information"]').tab('show');
                };
            }
        };
    }
);