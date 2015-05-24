'use strict';

angular.module('edup.tabs')

    .controller('StudentsTabController', function ($scope, RestService) {

        RestService.Students.get().then(function (result) {
            $scope.students = result.values;
            if ($scope.students.length > 0) {
                $scope.selectedStudent = $scope.students[0];
            }
        });

        $scope.setSelected = function (studentId) {
            $scope.selectedStudent = _.find($scope.students, function (student) {
                return studentId === student.id;
            });
        };
    }
);

